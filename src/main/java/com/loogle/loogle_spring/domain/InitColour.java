package com.loogle.loogle_spring.domain;

import com.loogle.loogle_spring.domain.Colour;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitColour {

    private final InitColourService initColourService;

    @PostConstruct
    public void init() {
        initColourService.init();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitColourService {

        private final EntityManager em;

        public void init() {
            Colour colour1 = createColour("블랙");
            Colour colour2 = createColour("화이트");
            Colour colour3 = createColour("그레이");
            Colour colour4 = createColour("레드");
            Colour colour5 = createColour("오렌지");
            Colour colour6 = createColour("옐로우");
            Colour colour7 = createColour("그린");
            Colour colour8 = createColour("블루");
            Colour colour9 = createColour("네이비");
            Colour colour10 = createColour("퍼플");
            Colour colour11 = createColour("핑크");
            Colour colour12 = createColour("브라운");
            Colour colour13 = createColour("베이지");
            Colour colour14 = createColour("카키");
            Colour colour15 = createColour("실버");
            Colour colour16 = createColour("골드");
            Colour colour17 = createColour("차콜");
            Colour colour18 = createColour("크림");
            Colour colour19 = createColour("스카이블루");
            Colour colour20 = createColour("민트");
            Colour colour21 = createColour("와인");
            Colour colour22 = createColour("카민");
            Colour colour23 = createColour("버건디");
            Colour colour24 = createColour("아이보리");
            Colour colour25 = createColour("기타");

            em.persist(colour1);
            em.persist(colour2);
            em.persist(colour3);
            em.persist(colour4);
            em.persist(colour5);
            em.persist(colour6);
            em.persist(colour7);
            em.persist(colour8);
            em.persist(colour9);
            em.persist(colour10);
            em.persist(colour11);
            em.persist(colour12);
            em.persist(colour13);
            em.persist(colour14);
            em.persist(colour15);
            em.persist(colour16);
            em.persist(colour17);
            em.persist(colour18);
            em.persist(colour19);
            em.persist(colour20);
            em.persist(colour21);
            em.persist(colour22);
            em.persist(colour23);
            em.persist(colour24);
            em.persist(colour25);


        }

        private Colour createColour(String colourName) {
            return new Colour(colourName);
        }
    }

}