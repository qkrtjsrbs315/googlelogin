package com.example.googlelogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private SignInButton btn_google; //구글 로그인 버튼
    private FirebaseAuth auth; //파이어 베이스 연동 객체
    private GoogleApiClient googleApiClient; //구글 API 클라이언트 객체
    private static final int REQ_SIGN_GOOGLE = 100; //구글 로그인 결과코드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this) //프래그먼트시 getContext
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions)
                .build();

        auth = FirebaseAuth.getInstance(); //파이어베이스 인증 객체 초기화
        btn_google = findViewById(R.id.btn_google);
        btn_google.setOnClickListener(new View.OnClickListener() { //구글로그인 버튼눌렀을때 수행
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,REQ_SIGN_GOOGLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //구글로그인인증요청했을때 결과값을 돌려받는곳
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_SIGN_GOOGLE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount(); //account 라는 데이터는 구글로그인 정보를 담고있습니다.(닉네임,프로필사진,url이메일주소 등..)
                resultLogin(account); //로그인 결과값 출력하라는 메소드
            }
        }


    }

    private void resultLogin(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"로그인성공",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                            intent.putExtra("nickname",account.getDisplayName()); //닉네임 가져올 수 있음
                            intent.putExtra("photourl",String.valueOf(account.getPhotoUrl())); //스트링으로 변환해서 전송

                            startActivity(intent);
                        } else{
                            Toast.makeText(MainActivity.this,"로그인실패",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}