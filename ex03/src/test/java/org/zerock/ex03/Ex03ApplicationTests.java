package org.zerock.ex03;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.ex03.DTO.SampleDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@SpringBootTest
class Ex03ApplicationTests {

	@Test
	void contextLoads() {

		List<SampleDTO> testArr = LongStream.rangeClosed(1, 20)
				.mapToObj(i->{
					return SampleDTO.builder().sno(i).first("f"+i).last("l"+i)
							.regTime(LocalDateTime.now())
							.build();
				}).collect(Collectors.toList());

		testArr.forEach(System.out::println);
	}

}
