# jwp-subway-path

## 기능 요구사항

### 역
- [x] 역은 고유한 식별자를 가진다.
- [x] 역 이름은 `역`으로 끝나야한다.
- [x] 역 이름은 1글자에서 11글자까지 가능하다.
- [x] 역 이름은 한글 + 숫자로만 이루어져야한다.

### 구역
- [x] 구역은 두 역과 역사이의 거리를 가진다.
- [x] 거리는 0 이상의 정수이고, 최대 15이다. 단위는 km이다.

### 노선
- [x] 노선 이름은 숫자 + `호선` 이다.
  - [x] 숫자는 1 ~ 9까지 가능하다.

- [ ] 역이 2개 이상 등록된 노선을 전부 보여주어야한다.

- [ ] 구역은 역 순서대로 저장되어 있어야 한다.
  - [ ] 노선 번호를 입력받으면, 노선에 포함된 역을 순서대로 보여주어야한다.

- [ ] 역을 추가할 수 있어야 한다.
  -[ ] 역은 원하는 위치에 자유롭게 등록할 수 있다.
  -[ ] 노선에 역이 등록될 때 거리 정보도 함께 포함되어야 한다.
  -[ ] 노선에 최초 등록 시 두 역을 동시에 등록해야 한다.
  -[ ] 하나의 역은 여러 노선에 등록될 수 있다.
  -[ ] 두 역의 가운데에 다른 역을 등록할 때, 기존 거리를 고려해야한다.

- [ ] 역을 제거할 수 있어야 한다.
  -[ ] 역을 제거할 경우 남은 역을 재배치 해야한다.
  -[ ] 노선에서 역이 제거될 경우 역과 역 사이의 거리도 재배정되어야한다.
  -[ ] 노선에 등록된 역이 2개 인 경우 하나의 역을 제거할 때 두 역이 모두 제거되어야한다.
