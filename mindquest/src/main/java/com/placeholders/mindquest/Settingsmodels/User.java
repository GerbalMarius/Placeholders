package com.placeholders.mindquest.Settingsmodels;

public class User {

   private String id;

   private String FirstName;

   private String LastName;

   private String Email;

   private String PassWord;
   private ProfilePhoto pfp;
   public User(String id, String firstName, String lastName, String email, String passWord) {
      this.id = id;
      FirstName = firstName;
      LastName = lastName;
      Email = email;
      PassWord = passWord;
   }

   public User(String id, String firstName, String lastName, String email, String passWord, ProfilePhoto pfp) {
      this.id = id;
      FirstName = firstName;
      LastName = lastName;
      Email = email;
      PassWord = passWord;
      this.pfp = pfp;
   }
   public User() {}

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getFirstName() {
      return FirstName;
   }

   public void setFirstName(String firstName) {
      FirstName = firstName;
   }

   public String getLastName() {
      return LastName;
   }

   public void setLastName(String lastName) {
      LastName = lastName;
   }

   public String getEmail() {
      return Email;
   }

   public void setEmail(String email) {
      Email = email;
   }

   public String getPassWord() {
      return PassWord;
   }

   public void setPassWord(String passWord) {
      PassWord = passWord;
   }

   public ProfilePhoto getPfp() {
      return pfp;
   }

   public void setPfp(ProfilePhoto pfp) {
      this.pfp = pfp;
   }
}
