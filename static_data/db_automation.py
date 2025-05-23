import pandas as pd
import mysql.connector

# CSV 파일 로드
df = pd.read_csv('food_data.csv')

# 필요한 열만 추출 및 결측값 제거
filtered_df = df[['식품명', '식품대분류명','에너지(kcal)', '단백질(g)', '지방(g)', '탄수화물(g)']].dropna()

# MySQL 연결
conn = mysql.connector.connect(
    host='localhost',
    user='root',
    password='root',
    database='balanceeat'
)
cursor = conn.cursor()

# INSERT 쿼리
insert_query = """
    INSERT INTO nutrition (food_name, category, energy, protein, fat, carbohydrate)
    VALUES (%s, %s, %s, %s, %s, %s)
"""

# 데이터 삽입
for _, row in filtered_df.iterrows():
    cursor.execute(insert_query, (
        row['식품명'],
        row['식품대분류명'],
        float(row['에너지(kcal)']),
        float(row['단백질(g)']),
        float(row['지방(g)']),
        float(row['탄수화물(g)'])
    ))

conn.commit()
cursor.close()
conn.close()
