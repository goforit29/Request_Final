package takbaeyu;

import takbaeyu.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler{
    @Autowired
    RequestRepository requestRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPayCanceled_CancelPol(@Payload PayCanceled payCanceled){

        if(payCanceled.isMe()){

            Optional<Request> requestOptional = requestRepository.findById(payCanceled.getRequestId());
            Request request = requestOptional.get();
            request.setStatus(payCanceled.getStatus());
            requestRepository.save(request);

            System.out.println("##### listener CancelPol : " + payCanceled.toJson());
        }
    }

}
