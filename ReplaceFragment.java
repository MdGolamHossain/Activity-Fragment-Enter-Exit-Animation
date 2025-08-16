Workflow: A Activity → C Fragment → B Activity → D Fragment → Back to A Activity (Fade Animation)

A Activity → C Fragment (initial load)
  
// A Activity এর onCreate এ
ReplaceFragment replaceFragment = new ReplaceFragment(this);
replaceFragment.replaceFragment(new CFragment());

// ReplaceFragment.java (Fragment load helper class)
public class ReplaceFragment {
    private AppCompatActivity activity;

    public ReplaceFragment(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.fade_in,    // নতুন fragment আসবে
                R.anim.fade_out,   // পুরানো fragment যাবে
                R.anim.fade_in,    // back চাপলে আসবে
                R.anim.fade_out    // back চাপলে যাবে
        );
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }
}


//==========================================================

  
C Fragment → B Activity (button click)

// CFragment.java এ
binding.btnGoToB.setOnClickListener(v -> {
    Intent intent = new Intent(requireActivity(), BActivity.class);
    startActivity(intent);
    requireActivity().overridePendingTransition(
            R.anim.fade_in,
            R.anim.fade_out
    );
});

  //-=====================================================================

B Activity → D Fragment (full-screen fragment load)

// BActivity.java এ
ReplaceFragment replaceFragment = new ReplaceFragment(this);
replaceFragment.replaceFragment(new DFragment());

//-===============================================================

D Fragment → Back to A Activity (custom back button)

// DFragment.java এ
binding.backBtn.setOnClickListener(v -> {
    requireActivity().finish(); // B Activity বন্ধ হবে
    requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
});
//======================================================



D Fragment → Back to A Activity (hardware back button) ata kaj kore na tai amader back hardware back button tar action fragment thake dorte hobe


// BActivity.java এ but ata kaj korbe na 
@Override
public void onBackPressed() {
    super.onBackPressed();
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);           //  not work
}



// ata use korle kaj korbo 


@Override
public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // Back button handle
    requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
        new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Activity A তে ফেরার জন্য
                requireActivity().finish();
                requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
}



























