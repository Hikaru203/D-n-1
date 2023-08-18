/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phones.ui;

import com.phones.helper.DateHelper;

/**
 *
 * @author kioys
 */
public class Student
{
  private static int idTemp = 1; // need a temp id
  private int id;
  private String name;
  
  Student(String name){ // each time we create new Student idTemp will be auto increas by 1
       this.id = idTemp++; // set id equal idTemp
       this.name = name;
  }
  public String toString(){
       return name +id + DateHelper.toString(DateHelper.now());
  }
}
