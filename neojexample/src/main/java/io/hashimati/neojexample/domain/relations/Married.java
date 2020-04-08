package io.hashimati.neojexample.domain.relations;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor; 

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Married 
{
    private Date from, to;
}