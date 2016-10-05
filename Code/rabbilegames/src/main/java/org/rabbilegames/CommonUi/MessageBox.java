package org.rabbilegames.CommonUi;

import java.text.MessageFormat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.rabbilegames.ResourceManager;

public class MessageBox
{
	public static void ShowToast(String message, Object... args)
	{
		ResourceManager.Get().activity.toastOnUiThread(MessageFormat.format(message, args), 20000);
	}

	public static void ShowInfo(String message, Object... args)
	{
		ShowInfo(message, null, args);
	}

	public static void ShowInfo(final String message, final DialogInterface.OnClickListener clickListener,
			final Object... args)
	{
		ResourceManager.Get().activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(ResourceManager.Get().activity);
				builder.setTitle(ResourceManager.GetAppName());
				builder.setMessage(String.format(message, args));
				builder.setPositiveButton("OK", clickListener);
				//builder.setNegativeButton("No", null);
				builder.show();
			}
		});
	}

	public static void ShowQuestion(final String message, final DialogInterface.OnClickListener clickListener,
			final Object... args)
	{
		ResourceManager.Get().activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(ResourceManager.Get().activity);
				builder.setTitle(ResourceManager.GetAppName());
				builder.setMessage(MessageFormat.format(message, args));
				builder.setPositiveButton("Yes", clickListener);
				builder.setNegativeButton("No", null);
				builder.show();
			}
		});
	}

	public static void ShowQuestion(final String message, final DialogInterface.OnClickListener yesClickListener,
			final DialogInterface.OnClickListener noClickListener, final Object... args)
	{
		ResourceManager.Get().activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(ResourceManager.Get().activity);
				builder.setTitle(ResourceManager.GetAppName());
				builder.setMessage(MessageFormat.format(message, args));
				builder.setPositiveButton("Yes", yesClickListener);
				builder.setNegativeButton("No", noClickListener);
				builder.show();
			}
		});
	}

	public static void ShowInputBox(final String message, final String input1, final String input2,
			final OnInputBoxClickListener clickListener, final Object... args)
	{
		ResourceManager.Get().activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(ResourceManager.Get().activity);
				LinearLayout lila1 = new LinearLayout(ResourceManager.Get().activity);
				lila1.setOrientation(1); //1 is for vertical orientation

				final TextView inputTextView1 = new TextView(ResourceManager.Get().activity);
				final TextView inputTextView2 = new TextView(ResourceManager.Get().activity);
				inputTextView1.setText(input1);
				inputTextView2.setText(input2);

				final EditText inputTextBox1 = new EditText(ResourceManager.Get().activity);
				final EditText inputTextBox2 = new EditText(ResourceManager.Get().activity);
				inputTextBox1.setInputType(InputType.TYPE_CLASS_NUMBER);
				inputTextBox2.setInputType(InputType.TYPE_CLASS_NUMBER);

				lila1.addView(inputTextView1);
				lila1.addView(inputTextBox1);
				lila1.addView(inputTextView2);
				lila1.addView(inputTextBox2);
				builder.setView(lila1);
				builder.setTitle(ResourceManager.GetAppName());
				builder.setMessage(String.format(message, args));
				builder.setPositiveButton("OK", new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						clickListener.OnClick(dialog, which, inputTextBox1.getText().toString(), inputTextBox2
								.getText()
								.toString());
					}
				});
				builder.setNegativeButton("Cancel", null);
				builder.show();
			}
		});
	}

	public static void ShowInputBox(final String message, final String input1,
			final OnInputBoxClickListener clickListener, final Object... args)
	{
		ResourceManager.Get().activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(ResourceManager.Get().activity);
				LinearLayout lila1 = new LinearLayout(ResourceManager.Get().activity);
				lila1.setOrientation(1); //1 is for vertical orientation

				final TextView inputTextView1 = new TextView(ResourceManager.Get().activity);
				inputTextView1.setText(input1);

				final EditText inputTextBox1 = new EditText(ResourceManager.Get().activity);
				inputTextBox1.setInputType(InputType.TYPE_CLASS_NUMBER);

				lila1.addView(inputTextView1);
				lila1.addView(inputTextBox1);
				builder.setView(lila1);
				//	builder.setTitle(ResourceManager.GetAppName());
				builder.setMessage(String.format(message, args));
				builder.setPositiveButton("OK", new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						clickListener.OnClick(dialog, which, inputTextBox1.getText().toString());
					}
				});
				builder.setNegativeButton("Cancel", null);
				builder.show();
			}
		});
	}

}
