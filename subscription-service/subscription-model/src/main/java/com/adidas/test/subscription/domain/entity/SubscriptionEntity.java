package com.adidas.test.subscription.domain.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name = SubscriptionEntity.SUBSCRIPTIONS)
@Table(name = "SUBS_SUBSCRIPTIONS")
public class SubscriptionEntity {

  public static final String SUBSCRIPTIONS = "SUBSCRIPTIONS";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
  private Long id;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CREATION_DATE", nullable = false, length = 7)
  private java.util.Date creationDate;

  @Column(name = "EMAIL", length = 250, nullable = false)
  private String email;

  @Column(name = "NAME", length = 250, nullable = false)
  private String name;

  @Column(name = "SURNAME1", length = 250)
  private String surname1;

  @Column(name = "SURNAME2", length = 250)
  private String surname2;

  @Column(name = "GENDER", length = 250)
  private String gender;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "BIRTH_DATE", nullable = false, length = 7)
  private java.util.Date birthDate;

  @Column(name = "CONSENT_FLAG", length = 1, nullable = false)
  private String consentFlag;

  @Column(name = "NEWSLETTER_ID", precision = 10, scale = 0, nullable = false)
  private Long newslettterId;

  @Column(name = "SEND_EMAIL_ID", precision = 10, scale = 0, nullable = true)
  private Long sendEmailId;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CANCEL_DATE", nullable = true, length = 7)
  private java.util.Date cancelDate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname1() {
    return surname1;
  }

  public void setSurname1(String surname1) {
    this.surname1 = surname1;
  }

  public String getSurname2() {
    return surname2;
  }

  public void setSurname2(String surname2) {
    this.surname2 = surname2;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  public String getConsentFlag() {
    return consentFlag;
  }

  public void setConsentFlag(String consentFlag) {
    this.consentFlag = consentFlag;
  }

  public Long getNewslettterId() {
    return newslettterId;
  }

  public void setNewslettterId(Long newslettterId) {
    this.newslettterId = newslettterId;
  }

  public Long getSendEmailId() {
    return sendEmailId;
  }

  public void setSendEmailId(Long sendEmailId) {
    this.sendEmailId = sendEmailId;
  }
}
