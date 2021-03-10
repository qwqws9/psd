package xyz.dunshow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "market_detail")
@Getter
@Setter
public class MarketDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int marketDetailSeq;
    @Column
    private int marketMstSeq;
    @Column
    private String itemName;
    @Column
    private String slotId;
    @Column
    private String choiceOption;
    @Column
    private String emblemName1;
    @Column
    private String emblemColor1;
    @Column
    private String emblemName2;
    @Column
    private String emblemColor2;
    @Column
    private String emblemName3;
    @Column
    private String emblemColor3;
}
