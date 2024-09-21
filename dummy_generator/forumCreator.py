from faker import Faker
import random
import pandas as pd
import uuid
import os
from tqdm import tqdm  # 진행 상태 표시를 위한 라이브러리
from datetime import datetime, timedelta  # 날짜 및 시간 관련 모듈

# 환경변수에서 최대 문자 수 가져오기 (기본값: 1000자)
MAX_CHARS = int(os.getenv('MAX_CHARS', 1000))

fake = Faker('ko_KR')  # locale 설정 정보
Faker.seed()  # 초기 seed 설정

repeat_count = 100000  # 생성할 더미 데이터 수

# tqdm을 사용하여 진행 상황 표시
# 게시글 해쉬 아이디 생성
forum_id = [str(uuid.uuid4()) for _ in tqdm(range(repeat_count), desc="Generating Forum IDs")]

# 작성자 id 생성 (0 ~ 99999 까지의 랜덤 숫자)
author = [random.randint(0, 99999) for _ in tqdm(range(repeat_count), desc="Generating Author IDs")]

# 제목 생성: 임의의 문장 (catch_phrase 사용)
title = [fake.catch_phrase() for _ in tqdm(range(repeat_count), desc="Generating Titles")]

# content 생성: MAX_CHARS 이하의 문장으로 길게 만들기
content = []
for _ in tqdm(range(repeat_count), desc="Generating Content"):
    # 길이를 MAX_CHARS 이하로 조정
    phrase = fake.catch_phrase()
    # catch_phrase를 반복하여 MAX_CHARS 이하로 구성
    while len(phrase) < MAX_CHARS:
        phrase += ' ' + fake.catch_phrase()  # catch_phrase를 추가
    content.append(phrase[:MAX_CHARS])  # 최대 문자 수 제한

# 조회수 생성
views = [random.randint(0, 10000) for _ in tqdm(range(repeat_count), desc="Generating Views")]

# 좋아요 수 생성
likes = [random.randint(0, 500) for _ in tqdm(range(repeat_count), desc="Generating Likes")]

# 2023년 1월 1일부터 2024년 9월 1일까지의 무작위 날짜 생성
start_date = datetime(2023, 1, 1)
end_date = datetime(2024, 9, 1)

def random_date(start, end):
    """주어진 범위 내의 무작위 날짜와 시간 생성"""
    return start + timedelta(
        seconds=random.randint(0, int((end - start).total_seconds())),
    )

# create_at 필드 생성
create_at = [random_date(start_date, end_date) for _ in tqdm(range(repeat_count), desc="Generating create_at Dates")]
print("create_at 생성 완료")

# update_at 필드 생성 (create_at과 동일한 값으로 초기화)
update_at = create_at.copy()
print("update_at 생성 완료")

# is_delete 필드 초기화 (모든 값 False로 설정)
is_delete = [False for _ in tqdm(range(repeat_count), desc="Initializing is_delete Field")]
print("is_delete 생성 완료")

# 데이터프레임으로 변환
df_forum = pd.DataFrame({
    'forum_id': forum_id,
    'author': author,
    'title': title,
    'content': content,
    'views': views,
    'likes': likes,
    'create_at': create_at,  # create_at 필드 추가
    'update_at': update_at,  # update_at 필드 추가 (create_at과 동일한 값으로 설정)
    'is_delete': is_delete   # is_delete 필드 추가
})

# 데이터프레임 확인
print(df_forum.head())

# CSV 파일로 저장
csv_file_path = "forums.csv"
df_forum.to_csv(csv_file_path, index=False, encoding='utf-8')

print(f"Records have been saved to {csv_file_path}")
