package com.pc.electronic.store.entities;



import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="orders")
public class Order {

	@Id
	private String orderId;
	private String orderStatus;
	private String paymentStatus;
	private int orderAmount;
	@Column(length=500)
	private String billingAddress;
	private String billingPhone;
	private String billingName;
	private Date orderedDate;
	private Date deliveredDate;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="user_id")
	private User user;
	@OneToMany(mappedBy="order", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<OrderItem> orderItems=new ArrayList<>();
}
