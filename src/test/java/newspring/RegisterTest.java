package newspring;

import newspring.forinject.Patient;
import newspring.forinject.Glucose;
import newspring.forinject.Water;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class RegisterTest {

    private Register register;

    @Before
    public void setUp() throws Exception {
        register = new Register();
    }

    @Test
    public void absentObjectReturnOptionalEmpty(){
        Optional<Object> absent = register.get("absent");
        assertFalse(absent.isPresent());
    }

    @Test
    public void canInsertIntoRegisterAndRetrieve(){
        Object one = new Object();
        Object two = new Object();
        register.add("one", one);
        register.add("two", two);

        Object returnedOne = register.get("one").get();
        Object returnedTwo = register.get("two").get();

        assertNotNull(returnedOne);
        assertSame(one, returnedOne);
        assertSame(two, returnedTwo);
        assertNotSame(returnedOne, returnedTwo);
    }

    @Test
    public void canInsertObjectWithoutName(){
        Glucose glucose = new Glucose();
        register.add(glucose);

        Water water = new Water();
        register.add(water);

        Glucose retrievedOne = register.get(Glucose.class).get();
        Water retrievedTwo = register.get(Water.class).get();

        assertSame(glucose, retrievedOne);
        assertSame(water, water);
    }

    @Test(expected = RuntimeException.class)
    public void cantInsertObjectWithExistingName(){
        register.add("same", new Object());
        register.add("same", new Object());
    }

    @Test
    public void shouldBeEnjected(){
        Glucose glucose = new Glucose();
        register.add(new Patient());
        register.add(glucose);
        register.inject();

        Patient injected = register.get(Patient.class).get();
        Glucose retrievedGlucose = injected.getGlucose();

        assertSame(glucose, retrievedGlucose);
    }
}
