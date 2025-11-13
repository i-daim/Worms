package org.example;
import org.junit.jupiter.api.Test;
import org.example.model.entites.Position;
import static org.junit.jupiter.api.Assertions.*;

    public class TestPosition {


        @Test
        public void testDeplacer(){
            Position a = new Position(1, 2);
            a.deplacer(5, 5);
            assertEquals(1, a.getX());
            assertEquals(2, a.getY());

        }
        @Test
        public void testPosition(){
            Position a = new Position(1, 2);
            a.deplacer(5, 5);
            assertEquals(1, a.getX());
            assertEquals(2, a.getY());
        }
        @Test
       public void testGetX(){
            Position a = new Position(1, 2);
            a.deplacer(5, 5);
            assertEquals(1, a.getX());
       }
        @Test
        public void testGetY(){
            Position a = new Position(1, 2);
            a.deplacer(5, 5);
            assertEquals(2, a.getY());
        }
        @Test
        public void testEquals(){
            Position p1 = new Position(10, 20);
            Position p2 = new Position(10, 20);
            Position p3 = new Position(5, 20);
            Position p4 = new Position(10, 30);
            assertTrue(p1.equals(p1));
            assertTrue(p1.equals(p2));
            assertTrue(p2.equals(p1));
            assertFalse(p1.equals(p3));
            assertFalse(p1.equals(p4));
            assertFalse(p2.equals(null));


        }

        @Test
        public void testToString(){
            Position a = new Position(1, 3);
            assertEquals("(1, 3)", a.toString());
        }

        @Test
        public void testHashCode(){
            Position a = new Position(1, 3);
            assertEquals(34, a.hashCode());
        }

    }


