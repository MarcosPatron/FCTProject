package com.example.myapplication;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

import com.example.myapplication.utils.RecyclerChat;

public class RecyclerChatTest {

    private RecyclerChat adapter;

    @Before
    public void setUp() {
        adapter = new RecyclerChat(
                Arrays.asList("Hola", "¿Qué tal?", "Procesando petición..."),
                Arrays.asList(true, false, true)
        );
    }

    @Test
    public void getItemCount_returnsCorrectSize() {
        assertEquals(3, adapter.getItemCount());
    }

    @Test
    public void getItemViewType_returnsCorrectValues() {
        assertEquals(1, adapter.getItemViewType(0)); // mensaje del usuario
        assertEquals(0, adapter.getItemViewType(1)); // mensaje recibido
        assertEquals(1, adapter.getItemViewType(2)); // mensaje del usuario
    }
}

