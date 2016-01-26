# mybatis-generator-core

<plugin>
					<groupId>org.mybatis.generator</groupId>
					<artifactId>mybatis-generator-maven-plugin</artifactId>
					<version>1.3.3-SNAPSHOT</version>
					<configuration>
						<configurationFile>src/main/resources/generatorConfig.xml</configurationFile>
						<verbose>true</verbose>
						<overwrite>true</overwrite>
					</configuration>
					<executions>
						<execution>
							<id>Generate MyBatis Artifacts</id>
							<goals>
								<goal>generate</goal>
							</goals>
						</execution>
					</executions>
					<dependencies>
						<dependency>
							<groupId>com.sfwl.mybatis.generator</groupId>
							<artifactId>mybatis-generator-core</artifactId>
							<version>1.3.4</version>
						</dependency>
					</dependencies>
				</plugin>
				
				others in extraConfig
custom made core for oracle
