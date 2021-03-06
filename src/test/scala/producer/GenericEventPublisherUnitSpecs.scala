package producer

import java.io.ByteArrayOutputStream
import java.util.Date

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import event.{BaseEvent, EventOffsetAndHashValue}
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite

/**
  * Created by prayagupd
  * on 1/15/17.
  */

case class SomethingHappenedEvent(eventOffset: Long, eventHashValue: Long, eventType: String, createdDate: Date)
  extends BaseEvent {
  override def copyy(eventOffset: Long, eventHashValue: Long): BaseEvent = {
    this.copy(eventOffset = eventOffset, eventHashValue = eventHashValue)
  }
}

class GenericEventPublisherUnitSpecs extends FunSuite with MockFactory {

  val genericEventPublishertPublisher = new GenericEventPublisher("someEventStream")
  genericEventPublishertPublisher.eventPublisher = mock[EventPublisher]

  test("delegates to the actual producer returned by factory") {
    val event = SomethingHappenedEvent(1, 2, classOf[SomethingHappenedEvent].getSimpleName, new Date())

    (genericEventPublishertPublisher.eventPublisher.publish _) expects event
    genericEventPublishertPublisher.publish(event)
  }
}
