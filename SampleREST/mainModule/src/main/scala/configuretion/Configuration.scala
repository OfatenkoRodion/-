package configuretion

import com.typesafe.config.{Config, ConfigFactory}

private[swagger] trait Configuration {
  protected def config: Config = ConfigFactory.load()

  protected lazy val swaggerUiEnabled =
    if (config.hasPath("swagger-ui-enabled"))
      config.getBoolean("swagger-ui-enabled")
    else
      false
}