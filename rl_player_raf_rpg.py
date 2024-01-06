import gym
import time
import numpy as np
import requests
import random
import json

class RafRpg(gym.Env):
  def __init__(self) -> None:
    super().__init__()
    self.url_root = "http://localhost:8080"
    self.prev_value = 0
    url = self.url_root+"/map/restart"
    payload={}
    headers = {}
    self.actions = [0, 1, 2, 3, 4]
    #uzvisica, kapija, planina, livada, voda, suma
    #>, |, $, _, -, +
    self.tiles = {'>': 1, '|': 2, '$': 3, '_': 4, '-': 5, '+': 6}
    #seljak, uskok, prodavac
    #V, B, M
    self.actors = {'V': 7, 'B': 8, 'M': 9}
    response = requests.request("PUT", url, headers=headers, data=payload)
    tt = response.json()
    print(tt,type(tt))

  def reset(self,number = -1):
    if number == -1:
      url = self.url_root+"/map/restart"
    else:
      url = self.url_root+f"/map/restart?map_number={number}"
    payload={}
    headers = {}
    response = requests.request("PUT", url, headers=headers, data=payload)
    output = response.json()
    return output

  def step(self,action):
    url_sufix = "wait"
    if action == 0:
      url_sufix = "up"
    elif action == 1:
      url_sufix = "down"
    elif action == 2:
      url_sufix = "left"
    elif action == 3:
      url_sufix = "right"
    elif action == 4:
      url_sufix = "wait"

    url = self.url_root + "/player/" + url_sufix
    payload={}
    headers = {}
    response = requests.request("PUT", url, headers=headers, data=payload)
    time.sleep(0.02)

    url = self.url_root + "/map/full/matrix"
    response = requests.request("GET", url, headers=headers, data=payload)
    next_observation = response.json()

    url = self.url_root + "/player/inventory/value"
    response = requests.request("GET", url, headers=headers, data=payload)
    reward = response.json()

    url = self.url_root + "/map/isover"
    response = requests.request("GET", url, headers=headers, data=payload)
    done = response.json()

    return next_observation,reward,done,{}

  def render(self):
    payload={}
    headers = {}

    url = self.url_root + "/map/full/matrix"
    response = requests.request("GET", url, headers=headers, data=payload)
    next_observation = response.json()

    return next_observation
  
  def find_player_state(self):
    matrix = self.render()

    for i in range(len(matrix)):
      for j in range(len(matrix[0])):
        if matrix[i][j] == 'P':
          return i*len(matrix) + j
        
  def get_local_player_state(self):
    matrix = self.render()
    posi = 0
    posj = 0

    for i in range(len(matrix)):
          for j in range(len(matrix[0])):
            if matrix[i][j] == 'P':
              posi = i
              posj = j
              break
      
    #We read the state up->right->down->left,
    #and concatenate the string
    up = matrix[posi-1][posj]
    down = matrix[posi+1][posj]
    left = matrix[posi][posj-1]
    right = matrix[posi][posj+1]
            
    state = str(up + right + down + left)
  
    return state
        
  def save_q_table(self, q_table):
    q_table_serializable = {key: value.tolist() if isinstance(value, np.ndarray) else value
                            for key, value in q_table.items()}

    with open('q_table.json', 'w') as json_file:
      json.dump(q_table_serializable, json_file)

  def read_q_table(self):
    with open('q_table.json', 'r') as json_file:
      q_table = json.load(json_file)

    return np.array(q_table)

game = RafRpg()

def train():
  #Q-learning settings
  action_space = 5
  q_table = {}
  #or read an existing q table
  #q_table = game.read_q_table()

  episodes = 100
  max_steps_per_episode = 1000

  learning_rate = 0.7
  discount_rate = 0.6

  exploration_rate = 1
  max_exploration_rate = 1
  min_exploration_rate = 0.01
  exploration_decay_rate = 0.01

  max_learning_rate = 0.9
  min_learning_rate = 0.1
  learning_rate_decay = 0.001

  max_discount_rate = 0.99
  min_discount_rate = 0.1
  discount_rate_decay = 0.001

  rewards_all_episodes = []

  for episode in range(episodes):
      #Start the game with the episode % 6 level
      #because there are 6 levels in total
      game.reset(episode%6)
      #state = game.find_player_state()
      state = game.get_local_player_state()
      done = False
      score = 0

      for step in range(max_steps_per_episode):
          
          lr = max(min_learning_rate, learning_rate * np.exp(-learning_rate_decay * episode))
          dr = max(min_discount_rate, discount_rate * np.exp(-discount_rate_decay * episode))
          lr = min(max_learning_rate, lr)
          dr = min(max_discount_rate, dr)

          #Exploit or explore the map?
          exploration_rate_threshold = random.uniform(0, 1)
          if exploration_rate_threshold > exploration_rate:
            #Exploit the map based on the best action
            #action = np.argmax(q_table[state, :])
            if state in q_table:
              action = np.argmax(q_table[state])
            else:
              q_table[state] = np.zeros(action_space)
              action = random.choice(game.actions)
          else:
            #Explore the map randomly
            action = random.choice(game.actions)

          #Do the action
          next_observation, reward, done, _ = game.step(action)

          #Calculate the new state
          #new_state = game.find_player_state()
          new_state = game.get_local_player_state()

          if new_state not in q_table:
            q_table[new_state] = np.zeros(action_space)

          #Update the q table using the bellmans optimality equation
          #q_table[state, action] = (1 - lr) * q_table[state, action] + \
          #                        lr * (reward + dr * np.max(q_table[new_state, :]))
          q_table[state][action] = (1 - lr) * q_table[state][action] + \
                                  lr * (reward + dr * np.max(q_table[new_state]))

          state = new_state
          score += reward

          if done:
            print(f"WIN after {step} steps!")
            break

      exploration_rate = min_exploration_rate + \
        (max_exploration_rate - min_exploration_rate) * np.exp(-exploration_decay_rate*episode)
      
      rewards_all_episodes.append(score)

      print(f"Episode {episode}, Score: {score}")

  game.save_q_table(q_table)

def play():
  #Q-learning settings
  action_space = 5
  q_table = {}
  #or read an existing q table
  #q_table = game.read_q_table()

  episodes = 10
  max_steps_per_episode = 1000

  exploration_rate = 1
  max_exploration_rate = 1
  min_exploration_rate = 0.01
  exploration_decay_rate = 0.01

  rewards_all_episodes = []

  for episode in range(episodes):
      #Start the game with the episode % 6 level
      #because there are 6 levels in total
      game.reset(episode%6)
      #state = game.find_player_state()
      state = game.get_local_player_state()
      done = False
      score = 0

      for step in range(max_steps_per_episode):
          
          #Exploit or explore the map?
          exploration_rate_threshold = random.uniform(0, 1)
          if exploration_rate_threshold > exploration_rate:
            #Exploit the map based on the best action
            #action = np.argmax(q_table[state, :])
            if state in q_table:
              action = np.argmax(q_table[state])
            else:
              q_table[state] = np.zeros(action_space)
              action = random.choice(game.actions)
          else:
            #Explore the map randomly
            action = random.choice(game.actions)

          #Do the action
          next_observation, reward, done, _ = game.step(action)

          #Calculate the new state
          #new_state = game.find_player_state()
          new_state = game.get_local_player_state()

          if new_state not in q_table:
            q_table[new_state] = np.zeros(action_space)

          state = new_state
          score += reward

          if done:
            print(f"WIN after {step} steps!")
            break
      
      exploration_rate = min_exploration_rate + \
        (max_exploration_rate - min_exploration_rate) * np.exp(-exploration_decay_rate*episode)

      rewards_all_episodes.append(score)

      print(f"Episode {episode}, Score: {score}")

play()