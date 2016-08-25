package com.rabidgremlin.mutters;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import com.rabidgremlin.mutters.core.TemplatedIntent;
import com.rabidgremlin.mutters.core.IntentMatch;
import com.rabidgremlin.mutters.core.TemplatedIntentMatcher;
import com.rabidgremlin.mutters.core.NumberSlot;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.core.TemplatedUtterance;


import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.CustomSlot;

public class TestIntentMatcher
{

	@Test
	public void testBasicMatching()
	{
		TemplatedIntent additionIntent = new TemplatedIntent("Addition");

		additionIntent.addUtterance(new TemplatedUtterance("What's {number1} + {number2}"));
		additionIntent.addUtterance(new TemplatedUtterance("What is {number1} + {number2}"));
		additionIntent.addUtterance(new TemplatedUtterance("Add {number1} and {number2}"));
		additionIntent.addUtterance(new TemplatedUtterance("{number1} plus {number2}"));

		NumberSlot number1 = new NumberSlot("number1");
		NumberSlot number2 = new NumberSlot("number2");

		additionIntent.addSlot(number1);
		additionIntent.addSlot(number2);

		TemplatedIntentMatcher matcher = new TemplatedIntentMatcher();
		matcher.addIntent(additionIntent);

		IntentMatch intentMatch = matcher.match("What is 1 + 5", new Context());

		assertThat(intentMatch, is(notNullValue()));
		assertThat(intentMatch.getIntent(), is(additionIntent));
		assertThat(intentMatch.getSlotMatches().size(), is(2));

		SlotMatch number1Match = intentMatch.getSlotMatches().get(number1);
		assertThat(number1Match, is(notNullValue()));
		assertThat(number1Match.getValue(), is(1l));

		SlotMatch number2Match = intentMatch.getSlotMatches().get(number2);
		assertThat(number2Match, is(notNullValue()));
		assertThat(number2Match.getValue(), is(5l));

	}
	
	@Test
	public void testBrokenMatch()
	{
		TemplatedIntent intent = new TemplatedIntent("Hello");

		intent.addUtterance(new TemplatedUtterance("hello"));
		intent.addUtterance(new TemplatedUtterance("hi"));
		intent.addUtterance(new TemplatedUtterance("hiya"));
		
		TemplatedIntentMatcher matcher = new TemplatedIntentMatcher();
		matcher.addIntent(intent);
		
		IntentMatch intentMatch = matcher.match("book this flight", new Context());

		assertThat(intentMatch, is(nullValue()));				
	}
	
	@Test
	public void testBrokenAirportMatch()
	{
		TemplatedIntent intent = new TemplatedIntent("GetAirport");
		intent.addUtterance(new TemplatedUtterance("{Airport}"));

		CustomSlot airportSlot = new CustomSlot("Airport", new String[]{"NEWCASTLE"});
		intent.addSlot(airportSlot);
		

		TemplatedIntentMatcher matcher = new TemplatedIntentMatcher();
		matcher.addIntent(intent);
		
		IntentMatch intentMatch = matcher.match("next friday", new Context());

		assertThat(intentMatch, is(nullValue()));	
	}

}
