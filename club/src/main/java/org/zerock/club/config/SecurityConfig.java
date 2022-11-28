package org.zerock.club.config;

/**
 * @Description : 해당 Class 는 시큐리티 관련 기느응ㄹ  쉽게 설정하기 위한 Class 이다
 *
 * 문제 : 현재  이전에 사용했었던 WebSecurityConfigurerAdapter class를 상속 받아 구현하려 했지만
 *            해당 Class 에 보안문제가 있어어 Deprecated 되어 사용할수 없게 됐음
 *
 *
 * 필터와 필터 체이닝
 * - 스프링 시큐리티에서 필터는 서블릿이나 jsp 에서 사용하는 필터와 같은 개념이다.
 *   ✔단! 스프링 시큐리티에서는 스프링의 빈과 연동할 수 있는 구조로 설계되어있고
 *        일반적인 필터는 스프링 빈을 사용할 수 없기 떄문에 별도의 클래스를 상속 받는 형태가 많다.
 *        
 *  - 스프링 시큐리티에서 가장 핵심적인 기능을 하는 필터는 <b>AuthenticationManager</b>이다
 *     - 해당 필터가 가진 인증처리 메서드는 파라미터도 Authentication 타입이고 반환 타입 또한
 *       Authentication 이다
 * */
public class SecurityConfig{

    //TODO BCrypuPasswordEncoder ..

}
