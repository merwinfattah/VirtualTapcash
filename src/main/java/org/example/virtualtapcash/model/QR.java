package org.example.virtualtapcash.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_QR_Payment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QR {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qr_id")
    private long qrId;

    @Column(name = "activation_time")
    private LocalDateTime activationTime;

    @Column(name = "is_active")
    private Boolean isActive;

    @Getter
    @ManyToOne
    @JoinColumn(name = "cardId", referencedColumnName = "cardId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TapcashCard card;

}

