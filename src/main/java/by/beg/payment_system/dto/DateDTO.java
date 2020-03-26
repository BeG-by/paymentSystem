package by.beg.payment_system.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateDTO {


    @NonNull
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date firstDate;

    @NonNull
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date secondDate;

}
