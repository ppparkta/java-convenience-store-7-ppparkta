# java-convenience-store-precourse

## 목표

## 고려한 부분

1. 파일명이 products.md나 promotions.md가 아닐 때
    - 파일명이나 확장자가 다른 경우에도 파일 형식이 맞다면 동일하게 처리한다.
2. 상품으로 상품명, 금액, 프로모션타입이 모두 동일한 값이 중복으로 들어올 때
    - 수량을 합산하여 계산한다.
3. 이름이 같은데 가격이 다른 상품의 경우 같은 상품으로 처리하고 예외처리한다.

## 구현 기능 목록

### 입력

#### 파일 입력

- [ ] 상품 목록을 파일로 입력받는다.
- [ ] 행사 목록을 파일로 입력받는다.
    - [ ] 한 상품이 두 개 이상의 프로모션을 갖는지 확인한다.
- [ ] (공통) 각 파일이 형식에 맞는지 확인한다.
- [ ] 상품 목록의 프로모션 종류와 행사 목록이 일치하는지 확인한다.

#### CLI 입력

- [ ] 구매할 상품과 수량을 입력받는다.
- [ ] 프로모션 적용 가능한 상품에 대해 수량을 추가할지 입력받는다.
- [ ] 프로모션 재고가 부족한 상품에 대해 일부 수량을 정가로 결제할지 입력받는다.
- [ ] 멤버십 할인 적용 여부를 입력받는다.
- [ ] 추가 구매 여부를 입력받는다. (반복)

### 주문

- [ ] 주문 목록을 생성한다.

### 주문 계산

- [ ] 최종 상품 금액을 계산한다.
    - [ ] 할인 전 금액 계산한다.
    - [ ] 프로모션을 계산한다.
    - [ ] 멤버십 할인을 계산한다.
    - [ ] 최종 결제금액을 계산한다.

### 주문 목록

- [ ] 주문 목록을 생성한다.
    - [ ] 구매할 상품의 수량이 있는지 확인한다.
    - [ ] 프로모션 적용 가능한 수량을 계산한다.

### 상품 관리

- [ ] 상품 재고를 추가한다.
    - [ ] 이미 존재하는 상품이라면 재고를 합산한다.
    - [ ] 존재하는 프로모션 타입인지 확인한다.
    - [ ] 상품의 프로모션 타입이 존재한다면 해당 상품의 다른 프로모션은 없는지 확인한다.
- [ ] 상품의 수량이 있는지 확인한다.

### 재고

- [x] 재고를 생성한다.
    - [x] 상품이 null인지 확인한다.
    - [x] 수량이 0 이상인지 확인한다.
- [ ] 상품 수량을 차감한다.

### 상품

- [x] 상품을 생성한다.
    - [x] 상품명이 null이나 빈문자열인지 확인한다.
    - [x] 가격이 0 이상인지 확인한다.
- [x] 상품을 비교하여 같은 값을 가진 상품인지 확인한다.

### 프로모션 타입 매니저

- [x] 프로모션 타입의 조회를 관리한다.
    - [x] 프로모션 이름이 중복되는지 확인한다.
- [x] 프로모션타입이 존재하는지 확인한다.

### 프로모션 타입

- [x] 프로모션 타입을 생성한다.
    - [x] 이름이 null이나 빈문자열인지 확인한다.
    - [x] buy와 get이 1이상인지 확인한다.
    - [x] 시작일이 종료일보다 앞인지 확인한다.
    - [x] 종료일이 현재날짜보다 뒤인지 확인한다.

### 프로모션

- [ ] 현재 날짜가 프로모션 기간이 지났는지 확인한다.
- [ ] 상품 수량이 프로모션 혜택과 맞아떨어지는지 계산한다.
- [ ] 프로모션 혜택을 몇 개 받을 수 있는지 계산한다.
- [ ] 상품을 추가 시 프로모션 혜택을 받을 수 있다면 몇 개 추가해야 하는지 계산한다.
- [ ] 재고 부족으로 인해 프로모션 혜택을 받지 못한다면 몇 개 상품에서 못 받는지 확인한다.

### 멤버십

- [ ] 멤버십 할인율을 계산한다.
- [ ] 할인금액을 최대한도로 제한한다.

### 출력

- [ ] 안내 문구 출력
- [ ] 보유 상품 출력
    - [ ] 재고가 0개라면 재고 없음을 출력한다.
- [ ] 영수증 출력
    - [ ] 원가 출력
    - [ ] 증정품 출력
    - [ ] 할인률 및 낼 돈 출력

## 클래스

<table>
</table>

## 패키지 구조

