package fr.epsi.montpellier.wsfoldermanagement.amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.epsi.montpellier.wsfoldermanagement.model.MQUserMessage;
import fr.epsi.montpellier.wsfoldermanagement.service.FoldersManagement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQReceiver {

    // @Autowired
    private final FoldersManagement foldersManagement;
    // Logger
    private static final Logger logger = LogManager.getLogger("MQReceiver");

    public RabbitMQReceiver(FoldersManagement foldersManagement) {
        this.foldersManagement = foldersManagement;
    }

    @RabbitListener(queues = "${rabbitmq.queueName}")
    public void receive(String message) throws InterruptedException {
        logger.info(String.format("receive: %s", message));
        /* Le message reçu est de type : {"status":"-1 | 0 | 1", "login": "prenom.nom", "message":"{Add|Delete} user"}
            Status :
                -1 => Delete user
                1  => Add user
         */
        // Conversion de string JSON en objet
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            MQUserMessage userMessage = objectMapper.readValue(message, MQUserMessage.class);
            switch (userMessage.getStatus()) {
                case -1: // DELETE
                    foldersManagement.deleteHomeDirectory(userMessage.getLogin());
                    logger.info(String.format("DELETE user(%s)", userMessage.getLogin()));
                    break;
                case 1: // ADD
                    foldersManagement.createHomeDirectory(userMessage.getLogin());
                    break;
                default:
                    // Do nothing
                    break;
            }
        // } catch (JsonProcessingException exception) {
        } catch (Exception exception) {
            logger.error(String.format("receive(%s)", message), exception);
        }
    }
}
