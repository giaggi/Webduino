package com.webduino.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.webduino.R;
import com.webduino.fragment.adapters.CardAdapter;
import com.webduino.fragment.adapters.HeaterDataRowItem;
import com.webduino.fragment.adapters.HeaterListListener;
import com.webduino.fragment.cardinfo.ActionButtonCardInfo;
import com.webduino.fragment.cardinfo.optioncardvalue.BooleanOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.DateOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.IntegerOptionCardValue;
import com.webduino.fragment.cardinfo.OptionCardInfo;
import com.webduino.fragment.cardinfo.TimeIntervalCardInfo;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.ProgramCardInfo;
import com.webduino.fragment.cardinfo.TriggerCardInfo;
import com.webduino.fragment.cardinfo.optioncardvalue.OptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.MultiChoiceOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.StringOptionCardValue;
import com.webduino.scenarios.Scenario;
import com.webduino.scenarios.ScenarioProgram;
import com.webduino.scenarios.ScenarioTimeInterval;
import com.webduino.scenarios.ScenarioTrigger;
import com.webduino.scenarios.Scenarios;

import java.util.ArrayList;
import java.util.List;

public class ScenarioFragment extends Fragment implements
        TimeIntervalFragment.OnTimeIntervalFragmentInteractionListener, TriggerFragment.OnTriggerFragmentInteractionListener, ProgramFragment.OnProgramFragmentInteractionListener {

    public boolean adaptercreated = false;
    ArrayList<HeaterDataRowItem> list = new ArrayList<>();
    OnScenarioFragmentInteractionListener mListener;
    private Scenario scenario;
    private CardAdapter optionsAdapter, calendarsAdapter, triggersAdapter, programsAdaptes;

    OptionCardInfo optionCard_Name, optionCard_Description, optionCard_Priority, optionCard_Enabled;

    public interface OnScenarioFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSaveTimeInterval(Scenario scenario);
    }

    public void setListener(OnScenarioFragmentInteractionListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        }
        View v = inflater.inflate(R.layout.fragment_scenario, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        RecyclerView optionList = (RecyclerView) v.findViewById(R.id.optionList);
        optionList.setHasFixedSize(false);
        optionList.setLayoutManager(linearLayoutManager);
        optionsAdapter = new CardAdapter(this, createOptionList());
        optionList.setAdapter(optionsAdapter);
        optionsAdapter.setListener(new CardAdapter.OnListener() {
            @Override
            public void onClick(int position, CardInfo cardInfo) {
                OptionCardInfo optionCardInfo = (OptionCardInfo) cardInfo;
                optionCardInfo.value.setListener(new OptionCardValue.OptionCardListener() {
                    @Override
                    public void onSetValue(Object value) {
                        optionsAdapter.notifyDataSetChanged();
                    }
                });
                optionCardInfo.value.showPicker();
            }
        });

        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        RecyclerView calendarList = (RecyclerView) v.findViewById(R.id.calendarList);
        calendarList.setHasFixedSize(false);
        calendarList.setLayoutManager(linearLayoutManager);
        calendarsAdapter = new CardAdapter(this, createCalendarList());
        calendarList.setAdapter(calendarsAdapter);
        calendarsAdapter.setListener(new CardAdapter.OnListener() {
            @Override
            public void onClick(int position, CardInfo cardInfo) {
                onTimeintervalClick(position,cardInfo);
            }
        });

        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        RecyclerView triggerList = (RecyclerView) v.findViewById(R.id.triggerList);
        triggerList.setHasFixedSize(false);
        triggerList.setLayoutManager(linearLayoutManager);
        triggersAdapter = new CardAdapter(this, createTriggerList());
        triggerList.setAdapter(triggersAdapter);
        triggersAdapter.setListener(new CardAdapter.OnListener() {
            @Override
            public void onClick(int position, CardInfo cardInfo) {
                onTriggerClick(position,cardInfo);
            }
        });
        //triggerList.setNestedScrollingEnabled(false);

        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        RecyclerView programList = (RecyclerView) v.findViewById(R.id.programList);
        programList.setHasFixedSize(false);
        programList.setLayoutManager(linearLayoutManager);
        programsAdaptes = new CardAdapter(this, createProgramList());
        programList.setAdapter(programsAdaptes);
        programsAdaptes.setListener(new CardAdapter.OnListener() {
            @Override
            public void onClick(int position, CardInfo cardInfo) {
                onProgramClick(position,cardInfo);
            }
        });

        ImageButton okbutton = v.findViewById(R.id.confirmButton);
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scenario.name = optionCard_Name.value.getStringValue();
                scenario.description = optionCard_Description.value.getStringValue();
                Object val = optionCard_Enabled.value;
                scenario.enabled = optionCard_Enabled.value.getBoolValue();
                scenario.priority = optionCard_Enabled.value.getIntValue();

                if (mListener != null) {
                    mListener.onSaveTimeInterval(scenario);
                }
                getActivity().getFragmentManager().popBackStack();
            }
        });

        ImageButton cancelbutton = v.findViewById(R.id.cancelButton);
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().popBackStack();
            }
        });

        return v;
    }


    public void onTimeintervalClick(int position, CardInfo cardInfo) {

        if (cardInfo instanceof TimeIntervalCardInfo) {

            TimeIntervalCardInfo timeIntervalCardInfo = (TimeIntervalCardInfo) cardInfo;
            showTimeintervalFragment(timeIntervalCardInfo);

        } else if (cardInfo instanceof ActionButtonCardInfo) {

            TimeIntervalCardInfo timeIntervalCardInfo = new TimeIntervalCardInfo();
            showTimeintervalFragment(timeIntervalCardInfo);
        }
    }

    public void onTriggerClick(int position, CardInfo cardInfo) {

        if (cardInfo instanceof TriggerCardInfo) {

            TriggerCardInfo triggerCardInfo = (TriggerCardInfo) cardInfo;
            showTriggerFragment(triggerCardInfo);

        } else if (cardInfo instanceof ActionButtonCardInfo) {

            TriggerCardInfo triggerCardInfo = new TriggerCardInfo();
            showTriggerFragment(triggerCardInfo);
        }
    }

    public void onProgramClick(int position, CardInfo cardInfo) {

        if (cardInfo instanceof ProgramCardInfo) {

            ProgramCardInfo programCardInfo = (ProgramCardInfo) cardInfo;
            showProgramFragment(programCardInfo);

        } else if (cardInfo instanceof ActionButtonCardInfo) {

            ProgramCardInfo timeIntervalCardInfo = new ProgramCardInfo();
            showProgramFragment(timeIntervalCardInfo);
        }
    }

    private void showTimeintervalFragment(TimeIntervalCardInfo timeIntervalCardInfo) {
        TimeIntervalFragment timeIntervalFragment = new TimeIntervalFragment();
        timeIntervalFragment.addListener(this);
        ScenarioTimeInterval timeInterval;
        if (timeIntervalCardInfo.id == 0)
            timeInterval = new ScenarioTimeInterval();
        else
            timeInterval = scenario.calendar.getTimeIntervalFromId(timeIntervalCardInfo.id);
        if (timeInterval != null) {
            timeIntervalFragment.timeInterval = timeInterval;

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, (Fragment) timeIntervalFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    private void showTriggerFragment(TriggerCardInfo triggerCardInfo) {
        TriggerFragment triggerFragment = new TriggerFragment();
        triggerFragment.addListener(this);

        ScenarioTrigger trigger;
        if (triggerCardInfo.id == 0)
            trigger = new ScenarioTrigger();
        else
            trigger = scenario.getTriggerFromId(triggerCardInfo.id);

        if (trigger != null){
            triggerFragment.trigger = trigger;

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, (Fragment) triggerFragment);
        ft.addToBackStack(null);
        ft.commit();
        }
    }

    private void showProgramFragment(ProgramCardInfo programCardInfo) {
        ProgramFragment programFragment = new ProgramFragment();
        programFragment.addListener(this);
        ScenarioProgram program;
        if (programCardInfo.id == 0)
            program = new ScenarioProgram();
        else
            program = scenario.getProgramFromId(programCardInfo.id);
        if (program != null) {
            programFragment.program = program;

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, (Fragment) programFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    public List<CardInfo> createOptionList() {
        List<CardInfo> result = new ArrayList<CardInfo>();

        optionCard_Name = new OptionCardInfo();
        optionCard_Name.value = new StringOptionCardValue("Nome", scenario.name);
        result.add(optionCard_Name);

        optionCard_Description = new OptionCardInfo();
        optionCard_Description.value = new StringOptionCardValue("Descrizione", scenario.description);
        result.add(optionCard_Description);

        optionCard_Priority = new OptionCardInfo();
        optionCard_Priority.value = new IntegerOptionCardValue("Priorità", scenario.priority);
        result.add(optionCard_Priority);

        optionCard_Enabled = new OptionCardInfo();
        optionCard_Enabled.value = new BooleanOptionCardValue("Stato", scenario.enabled, "Abilitato", "Disabilitato");
        result.add(optionCard_Enabled);

        return result;
    }

    public List<CardInfo> createCalendarList() {
        List<CardInfo> result = new ArrayList<CardInfo>();
        for (ScenarioTimeInterval timeInterval : scenario.calendar.timeintervals) {
            TimeIntervalCardInfo timeintervalcardinfo = new TimeIntervalCardInfo();
            timeintervalcardinfo.id = timeInterval.id;
            timeintervalcardinfo.name = timeInterval.name;
            timeintervalcardinfo.setEnabled(timeInterval.enabled);
            result.add(timeintervalcardinfo);
        }

        CardInfo addButton = new ActionButtonCardInfo();
        addButton.id = 0;
        addButton.name = "Aggiungi";
        addButton.label = " ";
        addButton.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.calendar, null);
        addButton.setColor(Color.BLUE);
        result.add(addButton);
        return result;
    }

    public List<CardInfo> createTriggerList() {
        List<CardInfo> result = new ArrayList<CardInfo>();
        for (ScenarioTrigger trigger : scenario.triggers) {
            TriggerCardInfo triggercardinfo = new TriggerCardInfo();
            triggercardinfo.id = trigger.id;
            triggercardinfo.name = trigger.name;
            triggercardinfo.setEnabled(trigger.enabled);
            result.add(triggercardinfo);
        }
        CardInfo addButton = new ActionButtonCardInfo();
        addButton.id = 0;
        addButton.name = "Aggiungi";
        addButton.label = " ";
        addButton.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.calendar, null);
        addButton.setColor(Color.BLUE);
        result.add(addButton);
        return result;
    }

    public List<CardInfo> createProgramList() {
        List<CardInfo> result = new ArrayList<CardInfo>();
        for (ScenarioProgram program : scenario.programs) {
            ProgramCardInfo programCardInfo = new ProgramCardInfo();
            programCardInfo.id = program.id;
            programCardInfo.name = program.name;
            programCardInfo.setEnabled(program.enabled);
            result.add(programCardInfo);
        }
        CardInfo addButton = new ActionButtonCardInfo();
        addButton.id = 0;
        addButton.name = "Aggiungi";
        addButton.label = " ";
        addButton.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.calendar, null);
        addButton.setColor(Color.BLUE);
        result.add(addButton);
        return result;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //String strtext = getArguments().getString("shieldid");
        //shieldId = Integer.valueOf(strtext);
        String strtext = getArguments().getString("id");
        int id = Integer.valueOf(strtext);
        if (id == 0)
            scenario = new Scenario();
        else
            scenario = Scenarios.getFromId(id);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void createAdapter() {
        adaptercreated = true;
    }

    public void destroyAdapter() {
        adaptercreated = false;
    }

    @Override
    public void onSaveTimeInterval(ScenarioTimeInterval timeInterval) {
        for (ScenarioTimeInterval ti : scenario.calendar.timeintervals) {
            if (ti.id == timeInterval.id) {
                scenario.calendar.timeintervals.remove(ti);
                scenario.calendar.timeintervals.add(timeInterval);
                return;
            }
        }
    }

    @Override
    public void onSaveTrigger(ScenarioTrigger trigger) {

    }

    @Override
    public void onSaveTrigger(ScenarioProgram trigger) {

    }
}