package com.loosoo100.campus100.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.loosoo100.campus100.R;


/**
 * APP版本更新弹出框
 *
 * @author yang
 */
public class AppUpdateDialog extends Dialog {

    public AppUpdateDialog(Context context, int theme) {
        super(context, theme);
    }

    public AppUpdateDialog(Context context) {
        super(context);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    public static class Builder {

        private Context context;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;

        private OnClickListener positiveButtonClickListener,
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

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public AppUpdateDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final AppUpdateDialog dialog = new AppUpdateDialog(context,
                    R.style.alertDialog);
            dialog.setCanceledOnTouchOutside(false);
            View layout = inflater.inflate(R.layout.dialog_app_update, null);
            dialog.setContentView(layout);
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
                layout.findViewById(R.id.ib_exit).setVisibility(View.GONE);
                ((Button) layout.findViewById(R.id.btn_exit))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.btn_exit))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                    dialog.dismiss();
                                }
                            });
                } else {
                    ((Button) layout.findViewById(R.id.btn_exit))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                }
            } else {
                layout.findViewById(R.id.btn_exit).setVisibility(View.GONE);
                layout.findViewById(R.id.ib_exit).setVisibility(View.VISIBLE);
                ((ImageButton) layout.findViewById(R.id.ib_exit))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
            }

            if (message != null) {
                String newMessage = message.replace("&", "\n");
                System.out.println(newMessage);
                ((TextView) layout.findViewById(R.id.tv_content))
                        .setText(newMessage);
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