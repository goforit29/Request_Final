package takbaeyu;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Request_table")
public class Request {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long memberId;
    private Long qty;
    private String status="Registered";

    @PostPersist
    public void onPostPersist(){
        Requested requested = new Requested();
        BeanUtils.copyProperties(this, requested);
        requested.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.


        try {
            Thread.sleep((long) (400 + Math.random() * 300));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        takbaeyu.external.Payment payment = new takbaeyu.external.Payment();
        // mappings goes here
        payment.setRequestId(this.getId());
        payment.setMemberId(this.getMemberId());
        payment.setStatus("Paid");
        RequestApplication.applicationContext.getBean(takbaeyu.external.PaymentService.class)
            .dopay(payment);


    }

    @PreUpdate
    public void onPreUpdate(){
        ReqCanceled reqCanceled = new ReqCanceled();
        BeanUtils.copyProperties(this, reqCanceled);
        reqCanceled.publishAfterCommit();


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }




}
