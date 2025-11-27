package com.example.demo.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AnagramCheckerRequest {
    private String input1;
    private String input2;

}
