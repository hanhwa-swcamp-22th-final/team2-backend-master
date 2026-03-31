-- ============================================================
-- Master Service 초기 데이터 (H2 호환)
-- ============================================================

-- 1. countries (국가) - 13개
INSERT INTO countries (country_code, country_name, country_name_kr) VALUES ('US', 'United States', '미국');
INSERT INTO countries (country_code, country_name, country_name_kr) VALUES ('KR', 'South Korea', '대한민국');
INSERT INTO countries (country_code, country_name, country_name_kr) VALUES ('JP', 'Japan', '일본');
INSERT INTO countries (country_code, country_name, country_name_kr) VALUES ('CN', 'China', '중국');
INSERT INTO countries (country_code, country_name, country_name_kr) VALUES ('DE', 'Germany', '독일');
INSERT INTO countries (country_code, country_name, country_name_kr) VALUES ('GB', 'United Kingdom', '영국');
INSERT INTO countries (country_code, country_name, country_name_kr) VALUES ('FR', 'France', '프랑스');
INSERT INTO countries (country_code, country_name, country_name_kr) VALUES ('VN', 'Vietnam', '베트남');
INSERT INTO countries (country_code, country_name, country_name_kr) VALUES ('TH', 'Thailand', '태국');
INSERT INTO countries (country_code, country_name, country_name_kr) VALUES ('SG', 'Singapore', '싱가포르');
INSERT INTO countries (country_code, country_name, country_name_kr) VALUES ('AU', 'Australia', '호주');
INSERT INTO countries (country_code, country_name, country_name_kr) VALUES ('IN', 'India', '인도');
INSERT INTO countries (country_code, country_name, country_name_kr) VALUES ('AE', 'United Arab Emirates', '아랍에미리트');

-- 2. incoterms (인코텀즈) - 11개
INSERT INTO incoterms (incoterm_code, incoterm_name, incoterm_name_kr, incoterm_description, incoterm_transport_mode, incoterm_seller_segments, incoterm_default_named_place) VALUES ('EXW', 'Ex Works', '공장인도', '매수인이 매도인의 영업장에서 물품을 인수', '복합운송', '매도인 영업장', '매도인 공장');
INSERT INTO incoterms (incoterm_code, incoterm_name, incoterm_name_kr, incoterm_description, incoterm_transport_mode, incoterm_seller_segments, incoterm_default_named_place) VALUES ('FCA', 'Free Carrier', '운송인인도', '매도인이 지정된 장소에서 운송인에게 물품 인도', '복합운송', '지정 장소', '매도인 영업장');
INSERT INTO incoterms (incoterm_code, incoterm_name, incoterm_name_kr, incoterm_description, incoterm_transport_mode, incoterm_seller_segments, incoterm_default_named_place) VALUES ('CPT', 'Carriage Paid To', '운송비지급인도', '매도인이 지정 목적지까지 운송비 부담', '복합운송', '지정 목적지', '목적지 터미널');
INSERT INTO incoterms (incoterm_code, incoterm_name, incoterm_name_kr, incoterm_description, incoterm_transport_mode, incoterm_seller_segments, incoterm_default_named_place) VALUES ('CIP', 'Carriage and Insurance Paid To', '운송비보험료지급인도', '매도인이 운송비와 보험료 부담', '복합운송', '지정 목적지', '목적지 터미널');
INSERT INTO incoterms (incoterm_code, incoterm_name, incoterm_name_kr, incoterm_description, incoterm_transport_mode, incoterm_seller_segments, incoterm_default_named_place) VALUES ('DAP', 'Delivered at Place', '도착장소인도', '매도인이 지정 목적지에서 양하 준비된 상태로 인도', '복합운송', '지정 목적지', '매수인 영업장');
INSERT INTO incoterms (incoterm_code, incoterm_name, incoterm_name_kr, incoterm_description, incoterm_transport_mode, incoterm_seller_segments, incoterm_default_named_place) VALUES ('DPU', 'Delivered at Place Unloaded', '도착지양하인도', '매도인이 지정 목적지에서 양하하여 인도', '복합운송', '지정 목적지', '목적지 터미널');
INSERT INTO incoterms (incoterm_code, incoterm_name, incoterm_name_kr, incoterm_description, incoterm_transport_mode, incoterm_seller_segments, incoterm_default_named_place) VALUES ('DDP', 'Delivered Duty Paid', '관세지급인도', '매도인이 수입통관 및 관세까지 부담', '복합운송', '지정 목적지', '매수인 영업장');
INSERT INTO incoterms (incoterm_code, incoterm_name, incoterm_name_kr, incoterm_description, incoterm_transport_mode, incoterm_seller_segments, incoterm_default_named_place) VALUES ('FAS', 'Free Alongside Ship', '선측인도', '매도인이 지정 선적항에서 본선 선측에 물품 인도', '해상운송', '선적항 선측', '선적항');
INSERT INTO incoterms (incoterm_code, incoterm_name, incoterm_name_kr, incoterm_description, incoterm_transport_mode, incoterm_seller_segments, incoterm_default_named_place) VALUES ('FOB', 'Free on Board', '본선인도', '매도인이 지정 선적항에서 본선에 물품 적재', '해상운송', '선적항 본선', '선적항');
INSERT INTO incoterms (incoterm_code, incoterm_name, incoterm_name_kr, incoterm_description, incoterm_transport_mode, incoterm_seller_segments, incoterm_default_named_place) VALUES ('CFR', 'Cost and Freight', '운임포함인도', '매도인이 지정 목적항까지 운임 부담', '해상운송', '목적항', '목적항');
INSERT INTO incoterms (incoterm_code, incoterm_name, incoterm_name_kr, incoterm_description, incoterm_transport_mode, incoterm_seller_segments, incoterm_default_named_place) VALUES ('CIF', 'Cost, Insurance and Freight', '운임보험료포함인도', '매도인이 운임과 보험료 부담', '해상운송', '목적항', '목적항');

-- 3. currencies (통화) - 6개
INSERT INTO currencies (currency_code, currency_name, currency_symbol) VALUES ('USD', 'US Dollar', '$');
INSERT INTO currencies (currency_code, currency_name, currency_symbol) VALUES ('EUR', 'Euro', '€');
INSERT INTO currencies (currency_code, currency_name, currency_symbol) VALUES ('KRW', 'Korean Won', '₩');
INSERT INTO currencies (currency_code, currency_name, currency_symbol) VALUES ('JPY', 'Japanese Yen', '¥');
INSERT INTO currencies (currency_code, currency_name, currency_symbol) VALUES ('CNY', 'Chinese Yuan', '¥');
INSERT INTO currencies (currency_code, currency_name, currency_symbol) VALUES ('GBP', 'British Pound', '£');

-- 4. ports (항구) - 15개
INSERT INTO ports (port_code, port_name, port_city, country_id) VALUES ('KRPUS', 'Port of Busan', '부산', 2);
INSERT INTO ports (port_code, port_name, port_city, country_id) VALUES ('KRINC', 'Port of Incheon', '인천', 2);
INSERT INTO ports (port_code, port_name, port_city, country_id) VALUES ('KRKAN', 'Port of Gwangyang', '광양', 2);
INSERT INTO ports (port_code, port_name, port_city, country_id) VALUES ('USNYC', 'Port of New York', 'New York', 1);
INSERT INTO ports (port_code, port_name, port_city, country_id) VALUES ('USLAX', 'Port of Los Angeles', 'Los Angeles', 1);
INSERT INTO ports (port_code, port_name, port_city, country_id) VALUES ('USHOU', 'Port of Houston', 'Houston', 1);
INSERT INTO ports (port_code, port_name, port_city, country_id) VALUES ('JPTYO', 'Port of Tokyo', 'Tokyo', 3);
INSERT INTO ports (port_code, port_name, port_city, country_id) VALUES ('JPOSA', 'Port of Osaka', 'Osaka', 3);
INSERT INTO ports (port_code, port_name, port_city, country_id) VALUES ('CNSHA', 'Port of Shanghai', 'Shanghai', 4);
INSERT INTO ports (port_code, port_name, port_city, country_id) VALUES ('CNSZX', 'Port of Shenzhen', 'Shenzhen', 4);
INSERT INTO ports (port_code, port_name, port_city, country_id) VALUES ('DEHAM', 'Port of Hamburg', 'Hamburg', 5);
INSERT INTO ports (port_code, port_name, port_city, country_id) VALUES ('GBLON', 'Port of London', 'London', 6);
INSERT INTO ports (port_code, port_name, port_city, country_id) VALUES ('SGSIN', 'Port of Singapore', 'Singapore', 10);
INSERT INTO ports (port_code, port_name, port_city, country_id) VALUES ('VNSGN', 'Port of Ho Chi Minh', 'Ho Chi Minh', 8);
INSERT INTO ports (port_code, port_name, port_city, country_id) VALUES ('AEJEA', 'Port of Jebel Ali', 'Dubai', 13);

-- 5. payment_terms (결제조건) - 5개
INSERT INTO payment_terms (payment_term_code, payment_term_name, payment_term_description) VALUES ('TT', 'Telegraphic Transfer', '전신환 송금');
INSERT INTO payment_terms (payment_term_code, payment_term_name, payment_term_description) VALUES ('LC', 'Letter of Credit', '신용장');
INSERT INTO payment_terms (payment_term_code, payment_term_name, payment_term_description) VALUES ('DP', 'Documents against Payment', '지급인도조건');
INSERT INTO payment_terms (payment_term_code, payment_term_name, payment_term_description) VALUES ('DA', 'Documents against Acceptance', '인수인도조건');
INSERT INTO payment_terms (payment_term_code, payment_term_name, payment_term_description) VALUES ('CAD', 'Cash against Documents', '서류상환불');

-- 6. clients (거래처) - 20개
INSERT INTO clients (client_code, client_name, client_name_kr, country_id, client_city, port_id, client_address, client_tel, client_email, payment_term_id, currency_id, client_manager, department_id, client_status, client_reg_date, created_at, updated_at)
VALUES ('CLI001', 'Global Tech Inc.', '글로벌테크', 1, 'New York', 4, '123 Broadway, New York, NY 10001', '+1-212-555-0100', 'contact@globaltech.com', 1, 1, '김영업', 1, 'ACTIVE', '2024-01-15', NOW(), NOW());
INSERT INTO clients (client_code, client_name, client_name_kr, country_id, client_city, port_id, client_address, client_tel, client_email, payment_term_id, currency_id, client_manager, department_id, client_status, client_reg_date, created_at, updated_at)
VALUES ('CLI002', 'Pacific Trading Co.', '퍼시픽트레이딩', 1, 'Los Angeles', 5, '456 Sunset Blvd, LA, CA 90028', '+1-310-555-0200', 'info@pacifictrading.com', 1, 1, '김영업', 1, 'ACTIVE', '2024-02-01', NOW(), NOW());
INSERT INTO clients (client_code, client_name, client_name_kr, country_id, client_city, port_id, client_address, client_tel, client_email, payment_term_id, currency_id, client_manager, department_id, client_status, client_reg_date, created_at, updated_at)
VALUES ('CLI003', 'Tokyo Electronics Ltd.', '도쿄일렉트로닉스', 3, 'Tokyo', 7, '1-1 Marunouchi, Chiyoda-ku, Tokyo', '+81-3-5555-0300', 'sales@tokyoelec.co.jp', 2, 4, '김영업', 1, 'ACTIVE', '2024-02-15', NOW(), NOW());
INSERT INTO clients (client_code, client_name, client_name_kr, country_id, client_city, port_id, client_address, client_tel, client_email, payment_term_id, currency_id, client_manager, department_id, client_status, client_reg_date, created_at, updated_at)
VALUES ('CLI004', 'Shanghai Import Export', '상하이무역', 4, 'Shanghai', 9, '100 Nanjing Road, Shanghai', '+86-21-5555-0400', 'trade@shanghaiie.cn', 2, 5, '이영업', 1, 'ACTIVE', '2024-03-01', NOW(), NOW());
INSERT INTO clients (client_code, client_name, client_name_kr, country_id, client_city, port_id, client_address, client_tel, client_email, payment_term_id, currency_id, client_manager, department_id, client_status, client_reg_date, created_at, updated_at)
VALUES ('CLI005', 'Hamburg Chemicals GmbH', '함부르크케미칼', 5, 'Hamburg', 11, 'Hafenstrasse 10, 20457 Hamburg', '+49-40-5555-0500', 'info@hamburgchem.de', 2, 2, '이영업', 1, 'ACTIVE', '2024-03-15', NOW(), NOW());
INSERT INTO clients (client_code, client_name, client_name_kr, country_id, client_city, port_id, client_address, client_tel, client_email, payment_term_id, currency_id, client_manager, department_id, client_status, client_reg_date, created_at, updated_at)
VALUES ('CLI006', 'London Materials Ltd.', '런던머티리얼즈', 6, 'London', 12, '10 Canary Wharf, London E14', '+44-20-5555-0600', 'procurement@londonmat.co.uk', 1, 6, '김영업', 1, 'ACTIVE', '2024-04-01', NOW(), NOW());
INSERT INTO clients (client_code, client_name, client_name_kr, country_id, client_city, port_id, client_address, client_tel, client_email, payment_term_id, currency_id, client_manager, department_id, client_status, client_reg_date, created_at, updated_at)
VALUES ('CLI007', 'Vietnam Manufacturing', '베트남제조', 8, 'Ho Chi Minh', 14, '72 Le Loi, District 1, HCMC', '+84-28-5555-0700', 'order@vnmanufacturing.vn', 1, 1, '이영업', 1, 'ACTIVE', '2024-04-15', NOW(), NOW());
INSERT INTO clients (client_code, client_name, client_name_kr, country_id, client_city, port_id, client_address, client_tel, client_email, payment_term_id, currency_id, client_manager, department_id, client_status, client_reg_date, created_at, updated_at)
VALUES ('CLI008', 'Singapore Solutions Pte.', '싱가포르솔루션즈', 10, 'Singapore', 13, '1 Raffles Place, Singapore 048616', '+65-6555-0800', 'biz@sgpsolutions.sg', 1, 1, '김영업', 1, 'ACTIVE', '2024-05-01', NOW(), NOW());
INSERT INTO clients (client_code, client_name, client_name_kr, country_id, client_city, port_id, client_address, client_tel, client_email, payment_term_id, currency_id, client_manager, department_id, client_status, client_reg_date, created_at, updated_at)
VALUES ('CLI009', 'Osaka Trading Corp.', '오사카트레이딩', 3, 'Osaka', 8, '2-3 Nakanoshima, Kita-ku, Osaka', '+81-6-5555-0900', 'trade@osakatrading.co.jp', 2, 4, '이영업', 1, 'ACTIVE', '2024-05-15', NOW(), NOW());
INSERT INTO clients (client_code, client_name, client_name_kr, country_id, client_city, port_id, client_address, client_tel, client_email, payment_term_id, currency_id, client_manager, department_id, client_status, client_reg_date, created_at, updated_at)
VALUES ('CLI010', 'Shenzhen Tech Co.', '선전테크', 4, 'Shenzhen', 10, '88 Shennan Road, Futian, Shenzhen', '+86-755-5555-1000', 'info@shenzhentech.cn', 3, 5, '김영업', 1, 'ACTIVE', '2024-06-01', NOW(), NOW());
INSERT INTO clients (client_code, client_name, client_name_kr, country_id, client_city, port_id, client_address, client_tel, client_email, payment_term_id, currency_id, client_manager, department_id, client_status, client_reg_date, created_at, updated_at)
VALUES ('CLI011', 'Houston Energy Corp.', '휴스턴에너지', 1, 'Houston', 6, '1000 Main St, Houston, TX 77002', '+1-713-555-1100', 'energy@houstonenergy.com', 1, 1, '이영업', 1, 'ACTIVE', '2024-06-15', NOW(), NOW());
INSERT INTO clients (client_code, client_name, client_name_kr, country_id, client_city, port_id, client_address, client_tel, client_email, payment_term_id, currency_id, client_manager, department_id, client_status, client_reg_date, created_at, updated_at)
VALUES ('CLI012', 'Dubai International Trading', '두바이국제무역', 13, 'Dubai', 15, 'Jebel Ali Free Zone, Dubai', '+971-4-555-1200', 'trade@dubaiintl.ae', 1, 1, '김영업', 1, 'ACTIVE', '2024-07-01', NOW(), NOW());
INSERT INTO clients (client_code, client_name, client_name_kr, country_id, client_city, port_id, client_address, client_tel, client_email, payment_term_id, currency_id, client_manager, department_id, client_status, client_reg_date, created_at, updated_at)
VALUES ('CLI013', 'Bangkok Polymers Co.', '방콕폴리머', 9, 'Bangkok', NULL, '99 Silom Road, Bangrak, Bangkok', '+66-2-555-1300', 'poly@bangkokpoly.th', 3, 1, '이영업', 1, 'ACTIVE', '2024-07-15', NOW(), NOW());
INSERT INTO clients (client_code, client_name, client_name_kr, country_id, client_city, port_id, client_address, client_tel, client_email, payment_term_id, currency_id, client_manager, department_id, client_status, client_reg_date, created_at, updated_at)
VALUES ('CLI014', 'Sydney Resources Pty.', '시드니리소시스', 11, 'Sydney', NULL, '200 George St, Sydney NSW 2000', '+61-2-5555-1400', 'resources@sydneyres.com.au', 1, 1, '김영업', 1, 'ACTIVE', '2024-08-01', NOW(), NOW());
INSERT INTO clients (client_code, client_name, client_name_kr, country_id, client_city, port_id, client_address, client_tel, client_email, payment_term_id, currency_id, client_manager, department_id, client_status, client_reg_date, created_at, updated_at)
VALUES ('CLI015', 'Mumbai Industries Ltd.', '뭄바이인더스트리', 12, 'Mumbai', NULL, 'Nariman Point, Mumbai 400021', '+91-22-5555-1500', 'sales@mumbaiind.in', 3, 1, '이영업', 1, 'ACTIVE', '2024-08-15', NOW(), NOW());
INSERT INTO clients (client_code, client_name, client_name_kr, country_id, client_city, port_id, client_address, client_tel, client_email, payment_term_id, currency_id, client_manager, department_id, client_status, client_reg_date, created_at, updated_at)
VALUES ('CLI016', 'Paris Luxe Materials', '파리럭스머티리얼', 7, 'Paris', NULL, '8 Rue de Rivoli, 75001 Paris', '+33-1-5555-1600', 'luxe@parisluxe.fr', 2, 2, '김영업', 1, 'ACTIVE', '2024-09-01', NOW(), NOW());
INSERT INTO clients (client_code, client_name, client_name_kr, country_id, client_city, port_id, client_address, client_tel, client_email, payment_term_id, currency_id, client_manager, department_id, client_status, client_reg_date, created_at, updated_at)
VALUES ('CLI017', 'Berlin Engineering AG', '베를린엔지니어링', 5, 'Berlin', 11, 'Unter den Linden 1, 10117 Berlin', '+49-30-5555-1700', 'eng@berlineng.de', 2, 2, '이영업', 1, 'ACTIVE', '2024-09-15', NOW(), NOW());
INSERT INTO clients (client_code, client_name, client_name_kr, country_id, client_city, port_id, client_address, client_tel, client_email, payment_term_id, currency_id, client_manager, department_id, client_status, client_reg_date, created_at, updated_at)
VALUES ('CLI018', 'Hanoi Import Co.', '하노이임포트', 8, 'Hanoi', 14, '36 Tran Hung Dao, Hoan Kiem, Hanoi', '+84-24-5555-1800', 'import@hanoiimport.vn', 1, 1, '김영업', 1, 'ACTIVE', '2024-10-01', NOW(), NOW());
INSERT INTO clients (client_code, client_name, client_name_kr, country_id, client_city, port_id, client_address, client_tel, client_email, payment_term_id, currency_id, client_manager, department_id, client_status, client_reg_date, created_at, updated_at)
VALUES ('CLI019', 'Incheon Partners Co.', '인천파트너스', 2, 'Incheon', 2, '송도국제대로 123, 인천', '+82-32-555-1900', 'partner@incheonpartners.kr', 1, 3, '이영업', 1, 'INACTIVE', '2024-10-15', NOW(), NOW());
INSERT INTO clients (client_code, client_name, client_name_kr, country_id, client_city, port_id, client_address, client_tel, client_email, payment_term_id, currency_id, client_manager, department_id, client_status, client_reg_date, created_at, updated_at)
VALUES ('CLI020', 'Yokohama Plastics Ltd.', '요코하마플라스틱', 3, 'Yokohama', 7, '2-1 Minato Mirai, Nishi-ku, Yokohama', '+81-45-5555-2000', 'plastics@yokohamaplast.co.jp', 2, 4, '김영업', 1, 'INACTIVE', '2024-11-01', NOW(), NOW());

-- 7. items (품목) - 20개
INSERT INTO items (item_code, item_name, item_name_kr, item_spec, item_unit, item_pack_unit, item_unit_price, item_weight, item_hs_code, item_category, item_status, item_reg_date, created_at, updated_at)
VALUES ('ITM001', 'EVA Film', 'EVA 필름', '0.38mm/Clear', 'KG', 'ROLL', 2.50, 25.000, '3920.10', '태양광소재', 'ACTIVE', '2024-01-10', NOW(), NOW());
INSERT INTO items (item_code, item_name, item_name_kr, item_spec, item_unit, item_pack_unit, item_unit_price, item_weight, item_hs_code, item_category, item_status, item_reg_date, created_at, updated_at)
VALUES ('ITM002', 'PV Backsheet', 'PV 백시트', 'TPT/White/1100mm', 'M2', 'ROLL', 1.80, 15.000, '3920.99', '태양광소재', 'ACTIVE', '2024-01-15', NOW(), NOW());
INSERT INTO items (item_code, item_name, item_name_kr, item_spec, item_unit, item_pack_unit, item_unit_price, item_weight, item_hs_code, item_category, item_status, item_reg_date, created_at, updated_at)
VALUES ('ITM003', 'Solar Cell', '태양전지셀', 'Mono PERC/182mm/23.5%', 'EA', 'BOX', 0.25, 0.010, '8541.40', '태양광소재', 'ACTIVE', '2024-02-01', NOW(), NOW());
INSERT INTO items (item_code, item_name, item_name_kr, item_spec, item_unit, item_pack_unit, item_unit_price, item_weight, item_hs_code, item_category, item_status, item_reg_date, created_at, updated_at)
VALUES ('ITM004', 'Junction Box', '정션박스', 'IP68/3 Diodes/MC4', 'EA', 'BOX', 3.20, 0.250, '8536.90', '태양광부품', 'ACTIVE', '2024-02-15', NOW(), NOW());
INSERT INTO items (item_code, item_name, item_name_kr, item_spec, item_unit, item_pack_unit, item_unit_price, item_weight, item_hs_code, item_category, item_status, item_reg_date, created_at, updated_at)
VALUES ('ITM005', 'Aluminum Frame', '알루미늄프레임', 'Anodized/Silver/35mm', 'SET', 'PALLET', 8.50, 3.500, '7610.90', '태양광부품', 'ACTIVE', '2024-03-01', NOW(), NOW());
INSERT INTO items (item_code, item_name, item_name_kr, item_spec, item_unit, item_pack_unit, item_unit_price, item_weight, item_hs_code, item_category, item_status, item_reg_date, created_at, updated_at)
VALUES ('ITM006', 'Tempered Glass', '강화유리', '3.2mm/AR Coated/Low Iron', 'EA', 'CRATE', 12.00, 8.000, '7007.19', '태양광소재', 'ACTIVE', '2024-03-15', NOW(), NOW());
INSERT INTO items (item_code, item_name, item_name_kr, item_spec, item_unit, item_pack_unit, item_unit_price, item_weight, item_hs_code, item_category, item_status, item_reg_date, created_at, updated_at)
VALUES ('ITM007', 'PV Ribbon', 'PV 리본', 'Copper/0.23x1.5mm', 'KG', 'SPOOL', 15.00, 5.000, '7408.29', '태양광소재', 'ACTIVE', '2024-04-01', NOW(), NOW());
INSERT INTO items (item_code, item_name, item_name_kr, item_spec, item_unit, item_pack_unit, item_unit_price, item_weight, item_hs_code, item_category, item_status, item_reg_date, created_at, updated_at)
VALUES ('ITM008', 'Silicone Sealant', '실리콘실란트', 'RTV/Black/310ml', 'EA', 'BOX', 4.50, 0.350, '3214.10', '접착제', 'ACTIVE', '2024-04-15', NOW(), NOW());
INSERT INTO items (item_code, item_name, item_name_kr, item_spec, item_unit, item_pack_unit, item_unit_price, item_weight, item_hs_code, item_category, item_status, item_reg_date, created_at, updated_at)
VALUES ('ITM009', 'MC4 Connector', 'MC4 커넥터', 'Male+Female/IP67/30A', 'SET', 'BOX', 1.20, 0.050, '8536.90', '태양광부품', 'ACTIVE', '2024-05-01', NOW(), NOW());
INSERT INTO items (item_code, item_name, item_name_kr, item_spec, item_unit, item_pack_unit, item_unit_price, item_weight, item_hs_code, item_category, item_status, item_reg_date, created_at, updated_at)
VALUES ('ITM010', 'Solar Module 400W', '태양광모듈 400W', 'Mono/400W/1722x1134mm', 'EA', 'PALLET', 120.00, 21.000, '8541.40', '태양광모듈', 'ACTIVE', '2024-05-15', NOW(), NOW());
INSERT INTO items (item_code, item_name, item_name_kr, item_spec, item_unit, item_pack_unit, item_unit_price, item_weight, item_hs_code, item_category, item_status, item_reg_date, created_at, updated_at)
VALUES ('ITM011', 'Solar Module 500W', '태양광모듈 500W', 'Mono/500W/2094x1134mm', 'EA', 'PALLET', 150.00, 26.500, '8541.40', '태양광모듈', 'ACTIVE', '2024-06-01', NOW(), NOW());
INSERT INTO items (item_code, item_name, item_name_kr, item_spec, item_unit, item_pack_unit, item_unit_price, item_weight, item_hs_code, item_category, item_status, item_reg_date, created_at, updated_at)
VALUES ('ITM012', 'Solar Module 600W', '태양광모듈 600W', 'Mono/600W/2384x1303mm', 'EA', 'PALLET', 180.00, 32.000, '8541.40', '태양광모듈', 'ACTIVE', '2024-06-15', NOW(), NOW());
INSERT INTO items (item_code, item_name, item_name_kr, item_spec, item_unit, item_pack_unit, item_unit_price, item_weight, item_hs_code, item_category, item_status, item_reg_date, created_at, updated_at)
VALUES ('ITM013', 'Micro Inverter', '마이크로인버터', '400W/Single Phase/WiFi', 'EA', 'BOX', 85.00, 1.200, '8504.40', '인버터', 'ACTIVE', '2024-07-01', NOW(), NOW());
INSERT INTO items (item_code, item_name, item_name_kr, item_spec, item_unit, item_pack_unit, item_unit_price, item_weight, item_hs_code, item_category, item_status, item_reg_date, created_at, updated_at)
VALUES ('ITM014', 'String Inverter 10kW', '스트링인버터 10kW', '10kW/Three Phase/MPPT', 'EA', 'PALLET', 650.00, 25.000, '8504.40', '인버터', 'ACTIVE', '2024-07-15', NOW(), NOW());
INSERT INTO items (item_code, item_name, item_name_kr, item_spec, item_unit, item_pack_unit, item_unit_price, item_weight, item_hs_code, item_category, item_status, item_reg_date, created_at, updated_at)
VALUES ('ITM015', 'Mounting Structure', '구조물', 'Ground Mount/Galvanized', 'SET', 'BUNDLE', 45.00, 15.000, '7308.90', '구조물', 'ACTIVE', '2024-08-01', NOW(), NOW());
INSERT INTO items (item_code, item_name, item_name_kr, item_spec, item_unit, item_pack_unit, item_unit_price, item_weight, item_hs_code, item_category, item_status, item_reg_date, created_at, updated_at)
VALUES ('ITM016', 'DC Cable', 'DC 케이블', 'PV1-F/4mm2/Black', 'M', 'DRUM', 0.80, 0.055, '8544.49', '케이블', 'ACTIVE', '2024-08-15', NOW(), NOW());
INSERT INTO items (item_code, item_name, item_name_kr, item_spec, item_unit, item_pack_unit, item_unit_price, item_weight, item_hs_code, item_category, item_status, item_reg_date, created_at, updated_at)
VALUES ('ITM017', 'AC Cable', 'AC 케이블', 'H07RN-F/3x2.5mm2', 'M', 'DRUM', 1.50, 0.095, '8544.49', '케이블', 'ACTIVE', '2024-09-01', NOW(), NOW());
INSERT INTO items (item_code, item_name, item_name_kr, item_spec, item_unit, item_pack_unit, item_unit_price, item_weight, item_hs_code, item_category, item_status, item_reg_date, created_at, updated_at)
VALUES ('ITM018', 'Combiner Box', '접속함', '6 String/DC Fuse/SPD', 'EA', 'BOX', 55.00, 5.000, '8537.10', '전기부품', 'ACTIVE', '2024-09-15', NOW(), NOW());
INSERT INTO items (item_code, item_name, item_name_kr, item_spec, item_unit, item_pack_unit, item_unit_price, item_weight, item_hs_code, item_category, item_status, item_reg_date, created_at, updated_at)
VALUES ('ITM019', 'Energy Storage 5kWh', '에너지저장장치 5kWh', 'LFP/5kWh/Wall Mount', 'EA', 'PALLET', 1200.00, 45.000, '8507.60', 'ESS', 'INACTIVE', '2024-10-01', NOW(), NOW());
INSERT INTO items (item_code, item_name, item_name_kr, item_spec, item_unit, item_pack_unit, item_unit_price, item_weight, item_hs_code, item_category, item_status, item_reg_date, created_at, updated_at)
VALUES ('ITM020', 'Monitoring System', '모니터링시스템', 'WiFi/4G/Cloud Based', 'SET', 'BOX', 200.00, 2.000, '9031.80', '모니터링', 'INACTIVE', '2024-10-15', NOW(), NOW());

-- 8. buyers (거래처 담당자) - 46개 (주요 거래처 위주)
-- CLI001 Global Tech Inc.
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (1, 'John Smith', 'Procurement Manager', 'j.smith@globaltech.com', '+1-212-555-0101', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (1, 'Sarah Johnson', 'Senior Buyer', 's.johnson@globaltech.com', '+1-212-555-0102', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (1, 'Mike Davis', 'Technical Director', 'm.davis@globaltech.com', '+1-212-555-0103', NOW(), NOW());
-- CLI002 Pacific Trading Co.
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (2, 'David Lee', 'Import Manager', 'd.lee@pacifictrading.com', '+1-310-555-0201', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (2, 'Jennifer Park', 'Buyer', 'j.park@pacifictrading.com', '+1-310-555-0202', NOW(), NOW());
-- CLI003 Tokyo Electronics Ltd.
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (3, 'Tanaka Yuki', 'Purchasing Manager', 'y.tanaka@tokyoelec.co.jp', '+81-3-5555-0301', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (3, 'Suzuki Kenji', 'Assistant Manager', 'k.suzuki@tokyoelec.co.jp', '+81-3-5555-0302', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (3, 'Yamamoto Aiko', 'Engineer', 'a.yamamoto@tokyoelec.co.jp', '+81-3-5555-0303', NOW(), NOW());
-- CLI004 Shanghai Import Export
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (4, 'Wang Lei', 'Trade Director', 'l.wang@shanghaiie.cn', '+86-21-5555-0401', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (4, 'Zhang Wei', 'Buyer', 'w.zhang@shanghaiie.cn', '+86-21-5555-0402', NOW(), NOW());
-- CLI005 Hamburg Chemicals GmbH
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (5, 'Hans Mueller', 'Procurement Head', 'h.mueller@hamburgchem.de', '+49-40-5555-0501', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (5, 'Anna Schmidt', 'Senior Buyer', 'a.schmidt@hamburgchem.de', '+49-40-5555-0502', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (5, 'Klaus Weber', 'Technical Buyer', 'k.weber@hamburgchem.de', '+49-40-5555-0503', NOW(), NOW());
-- CLI006 London Materials Ltd.
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (6, 'James Wilson', 'Managing Director', 'j.wilson@londonmat.co.uk', '+44-20-5555-0601', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (6, 'Emily Brown', 'Buyer', 'e.brown@londonmat.co.uk', '+44-20-5555-0602', NOW(), NOW());
-- CLI007 Vietnam Manufacturing
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (7, 'Nguyen Van An', 'General Manager', 'an.nguyen@vnmanufacturing.vn', '+84-28-5555-0701', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (7, 'Tran Thi Mai', 'Procurement Officer', 'mai.tran@vnmanufacturing.vn', '+84-28-5555-0702', NOW(), NOW());
-- CLI008 Singapore Solutions Pte.
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (8, 'Tan Wei Ming', 'Operations Director', 'wm.tan@sgpsolutions.sg', '+65-6555-0801', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (8, 'Lim Mei Ling', 'Procurement Manager', 'ml.lim@sgpsolutions.sg', '+65-6555-0802', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (8, 'Ng Kah Seng', 'Engineer', 'ks.ng@sgpsolutions.sg', '+65-6555-0803', NOW(), NOW());
-- CLI009 Osaka Trading Corp.
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (9, 'Sato Hiroshi', 'Division Head', 'h.sato@osakatrading.co.jp', '+81-6-5555-0901', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (9, 'Ito Sakura', 'Buyer', 's.ito@osakatrading.co.jp', '+81-6-5555-0902', NOW(), NOW());
-- CLI010 Shenzhen Tech Co.
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (10, 'Li Ming', 'CEO', 'm.li@shenzhentech.cn', '+86-755-5555-1001', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (10, 'Chen Xiao', 'Technical Manager', 'x.chen@shenzhentech.cn', '+86-755-5555-1002', NOW(), NOW());
-- CLI011 Houston Energy Corp.
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (11, 'Robert Taylor', 'VP of Procurement', 'r.taylor@houstonenergy.com', '+1-713-555-1101', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (11, 'Lisa Anderson', 'Supply Chain Manager', 'l.anderson@houstonenergy.com', '+1-713-555-1102', NOW(), NOW());
-- CLI012 Dubai International Trading
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (12, 'Ahmed Al Rashid', 'Managing Partner', 'a.rashid@dubaiintl.ae', '+971-4-555-1201', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (12, 'Fatima Hassan', 'Import Coordinator', 'f.hassan@dubaiintl.ae', '+971-4-555-1202', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (12, 'Omar Khalil', 'Buyer', 'o.khalil@dubaiintl.ae', '+971-4-555-1203', NOW(), NOW());
-- CLI013 Bangkok Polymers Co.
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (13, 'Somchai Prasert', 'Director', 's.prasert@bangkokpoly.th', '+66-2-555-1301', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (13, 'Pranee Suksai', 'Purchasing', 'p.suksai@bangkokpoly.th', '+66-2-555-1302', NOW(), NOW());
-- CLI014 Sydney Resources Pty.
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (14, 'Chris Thompson', 'Operations Manager', 'c.thompson@sydneyres.com.au', '+61-2-5555-1401', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (14, 'Olivia Martin', 'Buyer', 'o.martin@sydneyres.com.au', '+61-2-5555-1402', NOW(), NOW());
-- CLI015 Mumbai Industries Ltd.
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (15, 'Rajesh Patel', 'Purchase Head', 'r.patel@mumbaiind.in', '+91-22-5555-1501', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (15, 'Priya Sharma', 'Senior Buyer', 'p.sharma@mumbaiind.in', '+91-22-5555-1502', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (15, 'Amit Singh', 'Quality Engineer', 'a.singh@mumbaiind.in', '+91-22-5555-1503', NOW(), NOW());
-- CLI016 Paris Luxe Materials
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (16, 'Pierre Dubois', 'Directeur Achats', 'p.dubois@parisluxe.fr', '+33-1-5555-1601', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (16, 'Marie Laurent', 'Acheteuse', 'm.laurent@parisluxe.fr', '+33-1-5555-1602', NOW(), NOW());
-- CLI017 Berlin Engineering AG
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (17, 'Friedrich Bauer', 'Einkaufsleiter', 'f.bauer@berlineng.de', '+49-30-5555-1701', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (17, 'Greta Hoffmann', 'Projektingenieurin', 'g.hoffmann@berlineng.de', '+49-30-5555-1702', NOW(), NOW());
-- CLI018 Hanoi Import Co.
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (18, 'Le Van Duc', 'Import Manager', 'duc.le@hanoiimport.vn', '+84-24-5555-1801', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (18, 'Pham Thi Lan', 'Coordinator', 'lan.pham@hanoiimport.vn', '+84-24-5555-1802', NOW(), NOW());
-- CLI019 Incheon Partners Co.
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (19, '박준호', '구매팀장', 'junho.park@incheonpartners.kr', '+82-32-555-1901', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (19, '최수정', '구매담당', 'sujung.choi@incheonpartners.kr', '+82-32-555-1902', NOW(), NOW());
-- CLI020 Yokohama Plastics Ltd.
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (20, 'Watanabe Koji', 'Manager', 'k.watanabe@yokohamaplast.co.jp', '+81-45-5555-2001', NOW(), NOW());
INSERT INTO buyers (client_id, buyer_name, buyer_position, buyer_email, buyer_tel, created_at, updated_at) VALUES (20, 'Nakamura Yui', 'Buyer', 'y.nakamura@yokohamaplast.co.jp', '+81-45-5555-2002', NOW(), NOW());
