export default function Home() {
  return (
    <main>
      <span className="tag">High-Traffic Drop System</span>
      <h1>KickRush Frontend</h1>
      <p>
        이 화면은 Spring Boot 기반 백엔드와 분리된 Next.js 프론트엔드의 시작점입니다.
        향후 실시간 재고, 대기열, 주문 흐름까지 확장해 고부하 상황을 시뮬레이션할 예정입니다.
      </p>

      <section className="section">
        <h2>현재 구성</h2>
        <div className="grid">
          <div className="card">
            <strong>Frontend</strong>
            <p>Next.js App Router, TypeScript</p>
          </div>
          <div className="card">
            <strong>Backend</strong>
            <p>Spring Boot 멀티 모듈 (kickrush-api/core/common)</p>
          </div>
          <div className="card">
            <strong>로드 테스트</strong>
            <p>가상 유저 시뮬레이션 예정 (k6/Locust 후보)</p>
          </div>
        </div>
      </section>

      <section className="section">
        <h2>다음 단계</h2>
        <div className="grid">
          <div className="card">
            <strong>API 연결</strong>
            <p>백엔드 엔드포인트와 연동해 실제 드롭 흐름을 구현합니다.</p>
          </div>
          <div className="card">
            <strong>성능 계측</strong>
            <p>RPS, 응답 시간, 오류율을 대시보드로 정리합니다.</p>
          </div>
          <div className="card">
            <strong>시각화</strong>
            <p>대기열과 재고 변화를 시각적으로 표현합니다.</p>
          </div>
        </div>
      </section>

      <div className="footer">KickRush v0.1 — frontend scaffolding</div>
    </main>
  );
}
