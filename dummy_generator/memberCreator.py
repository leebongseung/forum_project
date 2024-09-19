from faker import Faker
import random
import pandas as pd
import uuid
import re

fake = Faker('ko_KR')  # locale 설정 정보
Faker.seed()  # 초기 seed 설정

repeat_count = 5  # 10만개 더미 데이터 생성
name = [fake.name() for i in range(repeat_count)]
# uuid 생성해서 중복안되게
member_id = [str(uuid.uuid4()) for _ in range(repeat_count)]

# 아이디는 5자 이상 20자 이하로 입력해주세요.
# 아이디는 영문 소문자, 숫자, 하이픈(-), 언더스코어(_)만 사용 가능합니다.
login_id = []
for _ in range(repeat_count):
    while True:
        id_length = random.randint(5, 20)
        id_chars = 'abcdefghijklmnopqrstuvwxyz0123456789-_'
        generated_id = ''.join(random.choices(id_chars, k=id_length))
        if re.match(r'^[a-z0-9-_]+$', generated_id):  # 정규표현식으로 체크
            login_id.append(generated_id)
            break

# 비밀번호는 대소문자, 숫자, 특수문자를 최소 1개 이상 포함하며, 10자 이상이어야 합니다.
password = []
for _ in range(repeat_count):
    while True:
        generated_password = fake.password(length=random.randint(10, 16), special_chars=True, digits=True, upper_case=True, lower_case=True)
        if (re.search(r'[A-Z]', generated_password) and
                re.search(r'[a-z]', generated_password) and
                re.search(r'\d', generated_password) and
                re.search(r'[\W_]', generated_password)):  # 특수문자 확인
            password.append(generated_password)
            break

email = [fake.unique.free_email() for _ in range(repeat_count)]

# 정규표현식 만족하기
# "^(01[0|1|6|7|8|9])-([0-9]{3,4})-([0-9]{4})$"
phone_number = [fake.phone_number() for _ in range(repeat_count)]

# 데이터프레임으로 변환 (예시)
df = pd.DataFrame({
    'name': name,
    'member_id': member_id,
    'login_id': login_id,
    'password': password,
    'email': email,
    'phone_number': phone_number
})

# 데이터프레임 확인
print(df.head())

# 데이터프레임으로 변환
df = pd.DataFrame()
df['name'] = name
df['member_id'] = member_id
df['login_id'] = login_id
df['password'] = password
df['email'] = email
df['phone_number'] = phone_number

# records로 변환
records = df.to_dict(orient='records')

# records 확인
print(records)
