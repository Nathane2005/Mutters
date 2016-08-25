package com.rabidgremlin.mutters.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.rabidgremlin.mutters.util.Utils;

public class TemplatedIntentMatcher implements IntentMatcher
{

	private List<TemplatedIntent> intents = new ArrayList<TemplatedIntent>();

	public void addIntent(TemplatedIntent intent)
	{
		intents.add(intent);
	}

	/* (non-Javadoc)
	 * @see com.rabidgremlin.mutters.core.IntentMatcher#match(java.lang.String, com.rabidgremlin.mutters.core.Context)
	 */
	@Override
	public IntentMatch match(String utterance, Context context)
	{

		CleanedInput cleanedUtterance = InputCleaner.cleanInput(utterance);

		for (TemplatedIntent intent : intents)
		{
			TemplatedUtteranceMatch utteranceMatch = intent.matches(cleanedUtterance, context);
			if (utteranceMatch.isMatched())
			{
				return new IntentMatch(intent, utteranceMatch.getSlotMatches());
			}
		}

		return null;
	}
	
	public List<TemplatedIntent> getIntents()
	{
		return Collections.unmodifiableList(intents);
	}

}
