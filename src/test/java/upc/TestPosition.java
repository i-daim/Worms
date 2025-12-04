package upc;
import org.junit.jupiter.api.Test;
import upc.model.entites.Position;
import static org.junit.jupiter.api.Assertions.*;

    public class TestPosition {


        @Test
        public void testDeplacer(){
            Position a = new Position(1, 2);
            Position b = a.deplacer(5, 5);

            assertEquals(1, a.getX());
            assertEquals(2, a.getY());

            assertEquals(6, b.getX());
            assertEquals(7, b.getY());
        }

        @Test
        public void testGetX(){
            Position a = new Position(15, 25);
            assertEquals(15, a.getX());
        }

        @Test
        public void testGetY(){
            Position a = new Position(15, 25);
            assertEquals(25, a.getY());
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

        @Test
        public void testDistanceAvecMemePosition(){
            Position p1 = new Position(10, 10);
            Position p2 = new Position(10, 10);

            assertEquals(0.0, p1.distanceAvec(p2), 0.001);
        }

        @Test
        public void testDistanceAvecPositionsDifferentes(){
            Position p1 = new Position(0, 0);
            Position p2 = new Position(3, 4);

            assertEquals(5.0, p1.distanceAvec(p2), 0.001);
        }

        @Test
        public void testDistanceAvecCoordonnees(){
            Position p = new Position(0, 0);

            assertEquals(5.0, p.distance(3, 4), 0.001);
        }

        @Test
        public void testDistanceAvecCoordonneesMemes(){
            Position p = new Position(10, 20);

            assertEquals(0.0, p.distance(10, 20), 0.001);
        }
        @Test
        public void testPositionsNegatives(){
            Position p = new Position(-5, -10);

            assertEquals(-5, p.getX());
            assertEquals(-10, p.getY());
        }

        @Test
        public void testDeplacerAvecNegatifs(){
            Position p1 = new Position(10, 10);
            Position p2 = p1.deplacer(-5, -3);

            assertEquals(5, p2.getX());
            assertEquals(7, p2.getY());
        }

        @Test
        public void testPositionZero(){
            Position p = new Position(0, 0);

            assertEquals(0, p.getX());
            assertEquals(0, p.getY());
        }

        @Test
        public void testDeplacerZero(){
            Position p1 = new Position(5, 5);
            Position p2 = p1.deplacer(0, 0);

            assertEquals(5, p2.getX());
            assertEquals(5, p2.getY());
        }





    }


