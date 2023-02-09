INSERT INTO users
(email, nickname, reg_dt, runner_play_count, runner_win_count, chaser_play_count, chaser_win_count)
VALUES
    ('ljc9393@nate.com', '큰개님', NOW(), 7, 4, 8, 3),
    ('chaen511@naver.com', '성곡초짱이채은', NOW(), 10, 3, 9, 2),
    ('ljc1025@nate.com', '노예킹', NOW(), 20, 10, 4, 1),
    ('lkc263@naver.com', '아리따움', NOW(), 40, 30, 20, 15),
    ('siganshoyou@google.com', '추노장인', NOW(), 30, 15, 10, 6);
insert into users
(reg_dt, catch_count, chaser_play_count, chaser_win_count, email, exp, is_deleted, is_manager, level, money, nickname, paper_count, path, save_name, runner_play_count, runner_win_count, phone)
values
(now(), 0, 11, 7, "opi6@hanmail.net", 0, 0, 0, 1, 0, "인의동큰손", 0, null, null, 30, 15, "01026896940"),
(now(), 0, 11, 7, "lce511@naver.com", 0, 0, 0, 1, 85900, "채은짱님", 0, null, null, 30, 15, "01084054759"),
(now(), 0, 11, 7, "souk0712@naver.com", 0, 0, 0, 1, 80000, "모카", 0, null, null, 30, 15, "01082860799"),
(now(), 0, 11, 7, "asdf@naver.com", 0, 0, 0, 1, 0, "아무", 0, null, null, 13, 15, "01515498451"),
(now(), 0, 11, 7, "qwer@naver.com", 0, 0, 0, 1, 524, "이름", 0, null, null, 51, 15, "01059519858"),
(now(), 0, 11, 7, "zxcv@naver.com", 0, 0, 0, 1, 5000, "짓기", 0, null, null, 30, 15, "01098751351"),
(now(), 0, 11, 7, "grg@naver.com", 0, 0, 0, 1, 800, "귀찮아죽겠음", 0, null, null, 30, 15, "01849878512"),
(now(), 0, 11, 7, "tnth@naver.com", 0, 0, 0, 1, 7000, "닉넴뭐하지", 0, null, null, 30, 15, "01012168849");


INSERT INTO rooms
(is_public, lat, lng, password, radius, title, host_id, year, month, day, hour, minute)
VALUES
    (false, 36.12345, 125.12345, 123, 500, '사이좋은 구미 친구들', 2, 2023, 02, 02, 14, 49),
    (true, 36.12345, 125.12345, null, 500, '구미 공개방임', 2, 2023, 02, 02, 14, 49),
    (true, 35.5534, 37.14123, null, 200, '드루와', 1, 2023, 02, 01, 10, 22),
    (true, 35.5534, 37.14123, 1234, 300, '드루와', 3, 2023, 02, 01, 14, 55),
    (true, 35.5534, 37.14123, null, 400, '드루와', 4, 2023, 02, 02, 1, 05);

INSERT INTO items
(description, img_path, name, price, for_runner)
VALUES
    ("자신의 위치를 드러내지 않고 가장 가까운 추노꾼의 위치를 확인할 수 있다.", "item/item1.png", "천리안", 1500, 1),
    ("추노꾼이 자신을 잡을 수 있는 범위를 축소한다.", "item/item2.png", "위장", 2000, 1),
    ("진짜 노비문서의 위치를 확인할 수 있다.", "item/item3.png", "확실한\n정보통", 1000, 1),
    ("먹물을 뿌려 내 화면을 가릴 수 있다.", "item/item4.png", "먹물탄", 1300, 1),
    ("30초간 노비의 위치를 지도에 표시할 수 있다.", "item/item5.png", "조명탄", 1500, 0),
    ("자신이 노비를 잡을 수 있는 범위를 확대할 수 있다.", "item/item6.png", "긴\n오랏줄", 2000, 0),
    ("노비 문서의 위치를 셔플할 수 있다.", "item/item7.png", "거짓\n정보통", 1000, 0),
    ("연기를 피워 내 화면을 가릴 수 있다.", "item/item8.png", "연막탄", 1300, 0);


/*- 천리안 : 자신의 위치를 드러내지 않고 가장 가까운 추노꾼의 위치를 확인할 수 있다.
- 위장 : 추노꾼이 자신을 잡을 수 있는 범위를 축소한다.
- 확실한 정보통 : 진짜 노비문서의 위치를 확인할 수 있다.
- 먹물탄 : 먹물을 뿌려 내 화면을 가릴 수 있다.
2. 추노꾼용
    - 조명탄 : n초간 노비의 위치를 지도에 표시할 수 있다.
        - 전체 노비 위치 잠깐 표시
    - 긴 오랏줄 : 자신이 노비를 잡을 수 있는 범위를 확대할 수 있다.
    - 거짓 정보통 : 노비 문서의 위치를 셔플할 수 있다.
    - ~~허수아비 : 노비 화면에 가짜 추노꾼 위치 띄우기~~
    - 연막탄: 연기를 피워 내 화면을 가릴 수 있다.*/