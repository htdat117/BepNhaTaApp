package com.example.bepnhataapp.common.model;

public class CustomerHealth {
    private long customerHealthID;
    private long customerID;
    private String gender;
    private int age;
    private double weight;
    private double height;
    private String bodyType;
    private String allergy;
    private String commonGoal;
    private double targetWeight;
    private double weightChangeRate;
    private String physicalActivityLevel;

    public CustomerHealth() {}

    // getters and setters
    public long getCustomerHealthID() { return customerHealthID; }
    public void setCustomerHealthID(long customerHealthID) { this.customerHealthID = customerHealthID; }

    public long getCustomerID() { return customerID; }
    public void setCustomerID(long customerID) { this.customerID = customerID; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public String getBodyType() { return bodyType; }
    public void setBodyType(String bodyType) { this.bodyType = bodyType; }

    public String getAllergy() { return allergy; }
    public void setAllergy(String allergy) { this.allergy = allergy; }

    public String getCommonGoal() { return commonGoal; }
    public void setCommonGoal(String commonGoal) { this.commonGoal = commonGoal; }

    public double getTargetWeight() { return targetWeight; }
    public void setTargetWeight(double targetWeight) { this.targetWeight = targetWeight; }

    public double getWeightChangeRate() { return weightChangeRate; }
    public void setWeightChangeRate(double weightChangeRate) { this.weightChangeRate = weightChangeRate; }

    public String getPhysicalActivityLevel() { return physicalActivityLevel; }
    public void setPhysicalActivityLevel(String physicalActivityLevel) { this.physicalActivityLevel = physicalActivityLevel; }
} 
