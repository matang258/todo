let backendHost;

const hostname = window && window.location && window.location.hostname;

if (hostname === "localhost") {
  backendHost = "http://localhost:8081";
  // backendHost = "http://prod-todo-api-service-test.ap-northeast-2.elasticbeanstalk.com";
  // backendHost = "http://prod-todo-api-service-work.ap-northeast-2.elasticbeanstalk.com";
} else {
  // backendHost = "https://api.fsoftwareengineer.com";
  // backendHost = "http://prod-todo-api-service-test.ap-northeast-2.elasticbeanstalk.com"; //앞에 http:// 필수
  backendHost = "http://prod-todo-api-service-work.ap-northeast-2.elasticbeanstalk.com";
}



export const API_BASE_URL = `${backendHost}`;
