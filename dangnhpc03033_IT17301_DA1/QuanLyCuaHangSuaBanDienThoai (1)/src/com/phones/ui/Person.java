/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phones.ui;

/**
 *
 * @author kioys
 */
public class Person
{
  // arguments are passed using the text field below this editor
  public static void main(String[] args)
  {
    Student student = null;
    for(int i = 0 ; i<5; i++){
    	student = new Student("SC");
      	System.out.println(student.toString()+(i+1));
    }
  }
}
