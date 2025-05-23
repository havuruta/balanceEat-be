import pandas as pd

df = pd.read_csv('food_data.csv')
categories = df['식품대분류명'].dropna().unique()

for c in sorted(categories):
    print(c)
