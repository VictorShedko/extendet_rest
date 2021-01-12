package com.epam.esm.gift_extended.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToMany
    private List<Certificate> certificates;
    @OneToOne
    private User customer;
    private Date orderDate;
    private Float orderCost;

    public Order(Integer id, List<Certificate> certificates, User customer, Date orderDate, Float orderCost) {
        this.id = id;
        this.certificates = certificates;
        this.customer = customer;
        this.orderDate = orderDate;
        this.orderCost = orderCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Order order = (Order) o;

        if (id != null ? !id.equals(order.id) : order.id != null)
            return false;
        if (certificates != null ? !certificates.equals(order.certificates) : order.certificates != null)
            return false;
        if (customer != null ? !customer.equals(order.customer) : order.customer != null)
            return false;
        if (orderDate != null ? !orderDate.equals(order.orderDate) : order.orderDate != null)
            return false;
        return orderCost != null ? orderCost.equals(order.orderCost) : order.orderCost == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (certificates != null ? certificates.hashCode() : 0);
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        result = 31 * result + (orderDate != null ? orderDate.hashCode() : 0);
        result = 31 * result + (orderCost != null ? orderCost.hashCode() : 0);
        return result;
    }

    public Order() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Float getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(Float orderCost) {
        this.orderCost = orderCost;
    }
}
