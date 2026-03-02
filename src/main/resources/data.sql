-- 유저 생성 (비밀번호: test1234)
INSERT INTO users (email, nickname, password, user_role, created_at, modified_at) VALUES
                                                                                      ('user1@test.com', '홍길동', '$2a$04$kXGHu2MD1xtzmL1yBaSFtOM3Fj4Tjhyj34kUbGherK.BYyYEJPLTW', 'USER', NOW(), NOW()),
                                                                                      ('user2@test.com', '김철수', '$2a$04$kXGHu2MD1xtzmL1yBaSFtOM3Fj4Tjhyj34kUbGherK.BYyYEJPLTW', 'USER', NOW(), NOW()),
                                                                                      ('user3@test.com', '이영희', '$2a$04$kXGHu2MD1xtzmL1yBaSFtOM3Fj4Tjhyj34kUbGherK.BYyYEJPLTW', 'USER', NOW(), NOW()),
                                                                                      ('admin@test.com', '관리자', '$2a$04$kXGHu2MD1xtzmL1yBaSFtOM3Fj4Tjhyj34kUbGherK.BYyYEJPLTW', 'ADMIN', NOW(), NOW());


-- 일정 생성
INSERT INTO todos (title, contents, weather, user_id, created_at, modified_at) VALUES
                                                                                   ('프로젝트 기획', '프로젝트 기획서 작성', '맑음', 1, '2025-01-15 10:00:00', '2025-01-15 10:00:00'),
                                                                                   ('코드 리뷰', 'PR 리뷰하기', '흐림', 1, '2025-01-16 11:00:00', '2025-01-16 11:00:00'),
                                                                                   ('회의 준비', '주간 회의 자료 준비', '비', 2, '2025-01-17 09:00:00', '2025-01-17 09:00:00'),
                                                                                   ('개발 작업', 'API 개발', '맑음', 2, '2025-01-18 14:00:00', '2025-01-18 14:00:00'),
                                                                                   ('테스트 작성', '단위 테스트 작성', '흐림', 3, '2025-01-19 15:00:00', '2025-01-19 15:00:00');

-- 담당자 추가
-- 일정 1번: 담당자 3명
INSERT INTO managers (todo_id, user_id) VALUES
                                            (1, 1),  -- 작성자
                                            (1, 2),  -- 추가 담당자 1
                                            (1, 3);  -- 추가 담당자 2

-- 일정 2번: 담당자 2명
INSERT INTO managers (todo_id, user_id) VALUES
                                            (2, 1),  -- 작성자
                                            (2, 2);  -- 추가 담당자

-- 일정 3번: 담당자 1명
INSERT INTO managers (todo_id, user_id) VALUES
                                            (3, 2);  -- 작성자

-- 일정 4번: 담당자 2명
INSERT INTO managers (todo_id, user_id) VALUES
                                            (4, 2),  -- 작성자
                                            (4, 3);  -- 추가 담당자

-- 일정 5번: 담당자 1명
INSERT INTO managers (todo_id, user_id) VALUES
                                            (5, 3);  -- 작성자

-- 댓글 추가
-- 일정 1번: 댓글 5개
INSERT INTO comments (contents, todo_id, user_id, created_at, modified_at) VALUES
                                                                               ('좋은 아이디어네요!', 1, 1, '2025-01-15 10:30:00', '2025-01-15 10:30:00'),
                                                                               ('진행 상황 공유 부탁드립니다.', 1, 2, '2025-01-15 11:00:00', '2025-01-15 11:00:00'),
                                                                               ('동의합니다.', 1, 3, '2025-01-15 11:30:00', '2025-01-15 11:30:00'),
                                                                               ('언제까지 완료 가능할까요?', 1, 1, '2025-01-15 12:00:00', '2025-01-15 12:00:00'),
                                                                               ('다음주까지 가능합니다.', 1, 2, '2025-01-15 12:30:00', '2025-01-15 12:30:00');

-- 일정 2번: 댓글 3개
INSERT INTO comments (contents, todo_id, user_id, created_at, modified_at) VALUES
                                                                               ('확인했습니다.', 2, 2, '2025-01-16 11:30:00', '2025-01-16 11:30:00'),
                                                                               ('수정사항 반영 부탁드립니다.', 2, 1, '2025-01-16 12:00:00', '2025-01-16 12:00:00'),
                                                                               ('완료했습니다.', 2, 2, '2025-01-16 13:00:00', '2025-01-16 13:00:00');

-- 일정 3번: 댓글 2개
INSERT INTO comments (contents, todo_id, user_id, created_at, modified_at) VALUES
                                                                               ('자료 준비 잘 되고 있나요?', 3, 1, '2025-01-17 10:00:00', '2025-01-17 10:00:00'),
                                                                               ('네, 거의 완료되었습니다.', 3, 2, '2025-01-17 10:30:00', '2025-01-17 10:30:00');

-- 일정 4번: 댓글 1개
INSERT INTO comments (contents, todo_id, user_id, created_at, modified_at) VALUES
                                                                               ('API 설계 문서 공유 부탁드립니다.', 4, 3, '2025-01-18 15:00:00', '2025-01-18 15:00:00');

-- 일정 5번: 댓글 0개