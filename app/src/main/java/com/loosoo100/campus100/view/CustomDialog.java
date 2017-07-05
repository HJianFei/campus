package com.loosoo100.campus100.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loosoo100.campus100.R;

/**
 * 自定义弹出框
 * 
 * @author yang
 * 
 */
public class CustomDialog extends Dialog {

	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}

	public CustomDialog(Context context) {
		super(context);
	}

	public static class Builder {

		private Context context;
		private String title;
		private String message;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;

		private DialogInterface.OnClickListener positiveButtonClickListener,
				negativeButtonClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}

		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		public Builder setPositiveButton(int positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = (String) context
					.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		public CustomDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final CustomDialog dialog = new CustomDialog(context,
					R.style.alertDialog);
			dialog.setCanceledOnTouchOutside(false);
			View layout = inflater.inflate(R.layout.dialog_confirm, null);
			dialog.setContentView(layout);
			if (title != null && !title.equals("")) {
				((TextView) layout.findViewById(R.id.tv_title)).setText(title);
				((TextView) layout.findViewById(R.id.tv_title))
						.setVisibility(View.VISIBLE);
			} else {
				((TextView) layout.findViewById(R.id.tv_title))
						.setVisibility(View.GONE);
			}
			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.btn_ok))
						.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					((Button) layout.findViewById(R.id.btn_ok))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									dialog.dismiss();
									positiveButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				} else {
					((Button) layout.findViewById(R.id.btn_ok))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									dialog.dismiss();
								}
							});
				}
			} else {
				layout.findViewById(R.id.btn_ok).setVisibility(View.GONE);
			}

			if (negativeButtonText != null) {
				((Button) layout.findViewById(R.id.btn_cancel))
						.setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					((Button) layout.findViewById(R.id.btn_cancel))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
									dialog.dismiss();
								}
							});
				} else {
					((Button) layout.findViewById(R.id.btn_cancel))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									dialog.dismiss();
								}
							});
				}
			} else {
				layout.findViewById(R.id.btn_cancel).setVisibility(View.GONE);
			}

			if (message != null) {
				((TextView) layout.findViewById(R.id.tv_content))
						.setText(message);
				// } else if (contentView != null) {
				// if no message set
				// add the contentView to the dialog body
				// ((LinearLayout) layout.findViewById(R.id.tv_content))
				// .removeAllViews();
				// ((LinearLayout)
				// layout.findViewById(R.id.tv_content)).addView(
				// contentView, new LayoutParams(
				// LayoutParams.WRAP_CONTENT,
				// LayoutParams.WRAP_CONTENT));
			}
			dialog.setContentView(layout);
			return dialog;
		}

	}

}