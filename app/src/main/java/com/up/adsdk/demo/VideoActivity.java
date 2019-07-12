package com.up.adsdk.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.up.ads.UPRewardVideoAd;
import com.up.ads.wrapper.video.UPRewardVideoAdListener;

public class VideoActivity extends Activity {
    private static final String TAG = "AdsSdk_demo";
    private int coins;

    Button mBtnPlay;
    Button mBtnVideo;
    TextView mText;
    TextView mCoin;
    Button mBtnDebugView;

    UPRewardVideoAd mVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        mBtnPlay = findViewById(R.id.btnPlay);
        mBtnVideo = findViewById(R.id.btnVideo);
        mText = findViewById(R.id.text);
        mCoin = findViewById(R.id.coin);
        mBtnDebugView = findViewById(R.id.btnDebugView);

        mBtnVideo.setEnabled(false);
        coins = 0;

        mVideoAd = UPRewardVideoAd.getInstance(VideoActivity.this);
        mVideoAd.setUpVideoAdListener(new UPRewardVideoAdListener() {
            @Override
            public void onVideoAdClicked() {
                Log.i(TAG, "mVideoAd is clicked");
            }

            @Override
            public void onVideoAdClosed() {
                Log.i(TAG, "mVideoAd is closed");

                if (mVideoAd.isReady()) {
                    new Handler(getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(VideoActivity.this);
                            builder.setTitle("Game Over");
                            builder.setMessage("再看一个, +300 coins");
                            builder.setNegativeButton("开始", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    showAd();
                                }
                            });
                            builder.show();
                        }
                    });
                } else {
                    mBtnVideo.setEnabled(false);
                }
            }

            @Override
            public void onVideoAdDisplayed() {
                Log.i(TAG, "mVideoAd is displayed");
            }

            @Override
            public void onVideoAdReward() {
                Log.i(TAG, "mVideoAd is rewarded");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        coins = coins + 300;
                        mCoin.setText(coins + " coins");
                    }
                });
            }

            @Override
            public void onVideoAdDontReward(String reason) {
                Log.i(TAG, "mVideoAd is not rewarded: " + reason);
            }
        });

        mBtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnPlay.setText("游戏中 ... ...");
                mBtnPlay.setEnabled(false);
                mBtnVideo.setEnabled(false);

                new Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gameOver();

                    }
                }, 10 * 1000);
            }
        });

        mBtnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnPlay.setText("开始游戏");

                final AlertDialog.Builder builder = new AlertDialog.Builder(VideoActivity.this);
                builder.setTitle("Game Over");
                builder.setMessage("看视频拿奖励, +300 coins");
                builder.setNegativeButton("开始", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showAd();
                    }
                });
                builder.show();
            }
        });

        mBtnDebugView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UPRewardVideoAd.showVideoDebugActivity(VideoActivity.this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mVideoAd != null)
            mVideoAd.destroy();
    }

    private void gameOver() {
        mBtnPlay.setText("游戏结束");
        mBtnPlay.setEnabled(true);

        if (mVideoAd.isReady()) {
            mBtnVideo.setEnabled(true);
        } else {
            Toast.makeText(this, "视频广告没有加载成功", Toast.LENGTH_SHORT).show();
            mBtnVideo.setEnabled(false);
        }
    }

    private void showAd() {
        if (mVideoAd.isReady()) {
            mVideoAd.show("game");
        } else {
            Toast.makeText(VideoActivity.this, "广告还没准备好", Toast.LENGTH_LONG).show();
        }
    }

}
