from faker import Faker
import random
import pandas as pd
import uuid
import os

# 환경변수에서 최대 문자 수 가져오기 (기본값: 1000자)
MAX_CHARS = int(os.getenv('MAX_CHARS', 1000))

fake = Faker('ko_KR')  # locale 설정 정보
Faker.seed()  # 초기 seed 설정

repeat_count = 1  # 생성할 더미 데이터 수

# 게시글 해쉬 아이디
forum_id = [str(uuid.uuid4()) for _ in range(repeat_count)]

# 0 ~ 99999 까지의 랜덤 숫자
# 작성자 id
author = [random.randint(0, 99999) for _ in range(repeat_count)]

# 제목: 임의의 문장 (catch_phrase 사용)
title = [fake.catch_phrase() for _ in range(repeat_count)]

# content: MAX_CHARS자 이하의 문장으로 길게 만들기
content = []
for _ in range(repeat_count):
    # 길이를 MAX_CHARS 이하로 조정
    phrase = fake.catch_phrase()
    # catch_phrase를 반복하여 MAX_CHARS 이하로 구성
    while len(phrase) < MAX_CHARS:
        phrase += ' ' + fake.catch_phrase()  # catch_phrase를 추가
    content.append(phrase[:MAX_CHARS])  # 최대 문자 수 제한

# 조회수
views = [random.randint(0, 10000) for _ in range(repeat_count)]

# 좋아요 수
likes = [random.randint(0, 500) for _ in range(repeat_count)]

# 데이터프레임으로 변환
df_forum = pd.DataFrame({
    'forum_id': forum_id,
    'author': author,
    'title': title,
    'content': content,
    'views': views,
    'likes': likes
})

# 데이터프레임 확인
print(df_forum.head())

# records로 변환
forum_records = df_forum.to_dict(orient='records')

# records 확인
print(forum_records)
