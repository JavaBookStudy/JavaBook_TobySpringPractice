package com.toby.week7.domain;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class User {

    private String id;
    private String name;
    private String password;
}
