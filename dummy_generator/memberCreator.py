from faker import Faker
import random
import pandas as pd
import uuid
import re
from tqdm import tqdm  # 진행 상태 표시
from datetime import datetime, timedelta  # 날짜 및 시간 관련 모듈

fake = Faker('ko_KR')  # locale 설정 정보
Faker.seed()  # 초기 seed 설정

repeat_count = 100000  # 10만개 더미 데이터 생성

# tqdm 사용하여 진행 상황 표시
name = [fake.name() for i in tqdm(range(repeat_count), desc="Generating Names")]
print("name 생성 완료")

# uuid 생성해서 중복안되게
member_id = [str(uuid.uuid4()) for _ in tqdm(range(repeat_count), desc="Generating Member IDs")]
print("member_id 생성 완료")

# 아이디는 5자 이상 20자 이하로 입력해주세요.
login_id = []
for _ in tqdm(range(repeat_count), desc="Generating Login IDs"):
    while True:
        id_length = random.randint(5, 20)
        id_chars = 'abcdefghijklmnopqrstuvwxyz0123456789-_'
        generated_id = ''.join(random.choices(id_chars, k=id_length))
        if re.match(r'^[a-z0-9-_]+$', generated_id):  # 정규표현식으로 체크
            login_id.append(generated_id)
            break
print("login_id 생성 완료")

# 비밀번호는 대소문자, 숫자, 특수문자를 최소 1개 이상 포함하며, 10자 이상이어야 합니다.
password = []
for _ in tqdm(range(repeat_count), desc="Generating Passwords"):
    while True:
        generated_password = fake.password(length=random.randint(10, 16), special_chars=True, digits=True, upper_case=True, lower_case=True)
        if (re.search(r'[A-Z]', generated_password) and
                re.search(r'[a-z]', generated_password) and
                re.search(r'\d', generated_password) and
                re.search(r'[\W_]', generated_password)):  # 특수문자 확인
            password.append(generated_password)
            break
print("password 생성 완료")

# 이메일 생성
email = [fake.unique.free_email() for _ in tqdm(range(repeat_count), desc="Generating Emails")]
print("email 생성 완료")

# 전화번호 생성
phone_number = [fake.phone_number() for _ in tqdm(range(repeat_count), desc="Generating Phone Numbers")]
print("phone_number 생성 완료")

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

# role 필드 초기화 (모든 값 "MEMBER"으로 설정)
role = ["MEMBER" for _ in tqdm(range(repeat_count), desc="Initializing role Field")]
print("role 생성 완료")

# 데이터프레임으로 변환 (예시)
df = pd.DataFrame({
    'name': name,
    'member_id': member_id,
    'login_id': login_id,
    'password': password,
    'email': email,
    'phone_number': phone_number,
    'create_at': create_at,  # create_at 필드 추가
    'update_at': update_at,  # update_at 필드 추가 (create_at과 동일한 값으로 설정)
    'is_delete': is_delete,  # is_delete 필드 추가
    'role': role             # role 필드 추가
})

# 데이터프레임 확인
print(df.head())

# CSV 파일로 저장
csv_file_path = "members.csv"
df.to_csv(csv_file_path, index=False, encoding='utf-8')
print(f"Records have been saved to {csv_file_path}")
