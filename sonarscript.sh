mvn clean verify sonar:sonar \
  -Dsonar.projectKey=websearch \
  -Dsonar.projectName='websearch' \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=sqp_8ad4af3ef878d56d3f8a1eedc99ee8ac11ce8cf6