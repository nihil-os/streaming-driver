package producer

import java.io.ByteArrayOutputStream
import java.util.Date

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import net.manub.embeddedkafka.{EmbeddedKafka, EmbeddedKafkaConfig}
import event.{BaseEvent, EventOffsetAndHashValue}
import org.scalatest.FunSuite

/**
  * Created by prayagupd
  * on 1/15/17.
  */
case class ItemSoldEvent(eventOffset: Long, eventHashValue: Long, eventType: String, createdDate: Date) extends BaseEvent {
  override def copyy(eventOffset: Long, eventHashValue: Long): BaseEvent = {
    this.copy(eventOffset = eventOffset, eventHashValue = eventHashValue)
  }

}

class GenericEventPublisherIntegrationSpecs extends FunSuite {

  val genericEventPublisher = new GenericEventPublisher("ItemsEventStream")

  test("publishes an event based on streaming-conf") {

    implicit val streamingConfig = EmbeddedKafkaConfig(kafkaPort = 9092, zooKeeperPort = 2181)

    EmbeddedKafka.start()

    val event = ItemSoldEvent(0, 0, classOf[ItemSoldEvent].getSimpleName, new Date())

    val persistedEvent1 = genericEventPublisher.publish(ItemSoldEvent(0, 0, classOf[ItemSoldEvent].getSimpleName, new Date()))
    assert(persistedEvent1.eventOffset == 0)

    val persistedEvent2 = genericEventPublisher.publish(ItemSoldEvent(1, 2, classOf[ItemSoldEvent].getSimpleName, new Date()))
    assert(persistedEvent2.eventOffset == 1)

    EmbeddedKafka.stop()
  }
}
