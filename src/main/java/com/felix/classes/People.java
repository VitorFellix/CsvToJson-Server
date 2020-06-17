package com.felix.classes;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class People {
	
	public People(String[] people) {
		super();
		int i = 0;
		// 0
		this.id = Long.parseLong(people[i]);
		i++;// 1
		this.gender = people[i];
		i++;// 2
		this.nameSet = people[i];
		i++;// 3
		this.pronounRef = people[i];
		i++;// 4 + 5
		this.name = people[i] + people[i + 1];
		i += 2;// 6
		this.address = people[i];
		i++;// 7
		this.city = people[i];
		i++;// 8
		this.state = people[i];
		i++;// 9
		this.zipcode = people[i];
		i++;// 10
		this.countryFull = people[i];
		i++;// 11
		this.email = people[i];
		i++;// 12
		this.username = people[i];
		i++;// 13
		this.password = people[i];
		i++;// 14
		this.telephoneNumber = people[i];
		i++;

		try {
			String[] temp = people[i].split("/");
			LocalDate date = LocalDate.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[0]),
					Integer.parseInt(temp[1]));
			// 15
			this.birthday = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
			i++;
			//System.out.println("util date: " + this.birthday.toString());
		} catch (Exception e) {	}
		// 16
		CCType = people[i];
		i++;
		// 17
		CCNumber = people[i];
		i++;
		// 18
		CVV2 = people[i];
		i++;
		try {
			String[] temp = people[i].split("/");
			//System.out.println(temp);
			LocalDate date = LocalDate.of(Integer.parseInt(temp[1]), 01,
					Integer.parseInt(temp[0]));
			//System.out.println("local date exp: " + date.format(formatter));
			// 19
			CCExpires = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
			i++;
			//System.out.println("util date exp: " + this.birthday.toString());
		} catch (Exception e) {	}
		// 20
		NationalID = people[i];
		i++;
		// 21
		Color = people[i];
		i++;
		// 22
		this.weight = Float.parseFloat(people[i]);
		i++;
		// 23
		this.height = Integer.parseInt(people[i]);
		i++;
		// 24
		GUID = people[i];
		i++;
	}

	public People(Long id, String gender, String nameSet, String pronounRef, String name, String address, String city,
			String state, String zipcode, String countryFull, String email, String username, String password,
			String telephoneNumber, Date birthday, String cCType, String cCNumber, String cVV2, Date cCExpires,
			String nationalID, String color, float weight, int height, String gUID) {
		super();
		try {
			this.id = id;
			this.gender = gender;
			this.nameSet = nameSet;
			this.pronounRef = pronounRef;
			this.name = name;
			this.address = address;
			this.city = city;
			this.state = state;
			this.zipcode = zipcode;
			this.countryFull = countryFull;
			this.email = email;
			this.username = username;
			this.password = password;
			this.telephoneNumber = telephoneNumber;
			this.birthday = birthday;
			CCType = cCType;
			CCNumber = cCNumber;
			CVV2 = cVV2;
			CCExpires = cCExpires;
			NationalID = nationalID;
			Color = color;
			this.weight = weight;
			this.height = height;
			GUID = gUID;
		} catch (Exception e) {
			System.out.println("ERRO DURANTE A CRIACAO DO OBJETO PESSOA");
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private Long id;
	private String gender;
	private String nameSet;
	private String pronounRef;
	private String name;
	private String address;
	private String city;
	private String state;
	private String zipcode;
	private String countryFull;
	private String email;
	private String username;
	private String password;
	private String telephoneNumber;
	private java.util.Date birthday;
	private String CCType;
	private String CCNumber;
	private String CVV2;
	private Date CCExpires;
	private String NationalID;
	private String Color;
	private float weight;
	private int height;
	private String GUID;

	public String allToString() {
		return "People [id=" + id + ", gender=" + gender + ", nameSet=" + nameSet + ", pronounRef=" + pronounRef
				+ ", name=" + name + ", address=" + address + ", city=" + city + ", state=" + state + ", zipcode="
				+ zipcode + ", countryFull=" + countryFull + ", email=" + email + ", username=" + username
				+ ", password=" + password + ", telephoneNumber=" + telephoneNumber + ", birthday=" + birthday
				+ ", CCType=" + CCType + ", CCNumber=" + CCNumber + ", CVV2=" + CVV2 + ", CCExpires=" + CCExpires
				+ ", NationalID=" + NationalID + ", Color=" + Color + ", weight=" + weight + ", height=" + height
				+ ", GUID=" + GUID + "]";
	}
	@Override
	public String toString() {
		return "id=" + id + ", name=" + name + ", gender=" + gender + ", username=" + username;
	}

}
