package jj

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._

class TestGetAccountByUserId extends Simulation{

  val httpProtocol = http
    .baseUrl("http://alb-pocket-dev-813463882.us-east-1.elb.amazonaws.com")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,/;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val expectedAccount="""{"accountNumber":"1653680745158"}"""
  val scn: ScenarioBuilder = scenario("test-getAccount-byUserId")
    .exec(http("test-getAccount-byUserId")
      .get("/account/accountNumber?userId=988b6140-ddf5-11ec-bfb6-4984ba70d27f")
      .check(status.is(200))
      .check( bodyString.saveAs( "RESPONSE_DATA" ) )
      .check(substring( expectedAccount ) )
    )

    .exec( session => {
      println((session("RESPONSE_DATA").as[String]))
      session
    })

    .pause(5)

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)

}
