package kr.co.won.address;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Column(length = 45)
    private String zipCode;

    private String roadAddress;

    private String detailAddress;

    @Override
    public String toString() {
        return roadAddress + " " + detailAddress;
    }

}
