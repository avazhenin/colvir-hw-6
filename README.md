# colvir-hw-6

# add to your /etc/hosts
localhost colvir-hw-6 keycloak

clear && mvn clean install && docker-compose --file docker-compose.yml up -d --build
